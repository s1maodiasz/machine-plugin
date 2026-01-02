package com.github.s1maodyasz.machine.database;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class MachineDatabase {

    private final MongoClient client;
    private final MachineRepository repository;
    private final MachineCache cache;
    private final ExecutorService executor;
    private final Duration operationTimeout;

    public MachineDatabase(@NotNull Plugin plugin) {
        final ConfigurationSection mongo = plugin.getConfig().getConfigurationSection("mongo");
        if (mongo == null) throw new IllegalArgumentException("Missing config section: mongo");

        final String url = mongo.getString("url");
        if (url == null || url.isBlank()) throw new IllegalArgumentException("Missing config: mongo.url");

        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(url))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyToConnectionPoolSettings(b -> b.minSize(2)
                        .maxSize(10)
                        .maxWaitTime(5, TimeUnit.SECONDS)
                        .maxConnectionIdleTime(1, TimeUnit.MINUTES))
                .applyToSocketSettings(
                        b -> b.connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS))
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        CodecRegistries.fromProviders(
                                PojoCodecProvider.builder().automatic(true).build())))
                .build();

        this.client = MongoClients.create(settings);

        final String databaseName = mongo.getString("databaseName", "machine_database");
        final String collectionName = mongo.getString("collectionName", "machine");

        final var collection = client.getDatabase(databaseName).getCollection(collectionName, Machine.class);

        this.repository = new MachineRepository(collection);
        this.cache = new MachineCache(plugin);

        this.executor = Executors.newFixedThreadPool(4);

        final long timeoutMillis = plugin.getConfig().getLong("machine.database.operationTimeoutMillis", 1500L);
        this.operationTimeout = Duration.ofMillis(timeoutMillis);
    }

    private <T> CompletableFuture<T> withTimeout(CompletableFuture<T> future) {
        return future.orTimeout(operationTimeout.toMillis(), TimeUnit.MILLISECONDS);
    }

    public @NotNull Optional<Machine> findSync(@NotNull UUID id) {
        return repository.findById(id).map(m -> {
            cache.put(m);
            return m;
        });
    }

    public @NotNull CompletableFuture<Optional<Machine>> findAsync(@NotNull UUID id) {
        return withTimeout(CompletableFuture.supplyAsync(() -> findSync(id), executor))
                .exceptionally(ex -> Optional.empty());
    }

    public @NotNull Optional<Machine> localizeSync(@NotNull MachineLocation location) {
        return cache.localize(location).or(() -> {
            final Optional<Machine> fromDb = repository.findByLocation(location);
            fromDb.ifPresent(cache::put);
            return fromDb;
        });
    }

    public @NotNull CompletableFuture<Optional<Machine>> localizeAsync(@NotNull MachineLocation location) {
        return withTimeout(CompletableFuture.supplyAsync(() -> localizeSync(location), executor))
                .exceptionally(ex -> Optional.empty());
    }

    public void saveSync(@NotNull Machine machine) {
        repository.upsert(machine);
        cache.put(machine);
    }

    public @NotNull CompletableFuture<Void> saveAsync(@NotNull Machine machine) {
        return withTimeout(CompletableFuture.runAsync(() -> saveSync(machine), executor))
                .exceptionally(ex -> null);
    }

    public void removeSync(@NotNull Machine machine) {
        repository.deleteById(machine.id());
        cache.remove(machine.id());
    }

    public @NotNull CompletableFuture<Void> removeAsync(@NotNull Machine machine) {
        return withTimeout(CompletableFuture.runAsync(() -> removeSync(machine), executor))
                .exceptionally(ex -> null);
    }

    public @NotNull CompletableFuture<Void> saveAllAsync(@NotNull List<Machine> machines) {
        return withTimeout(CompletableFuture.runAsync(
                        () -> {
                            if (machines.isEmpty()) return;
                            repository.upsertAll(machines);
                            for (Machine m : machines) cache.put(m);
                        },
                        executor))
                .exceptionally(ex -> null);
    }

    public void close() {
        executor.close();
        client.close();
    }
}
