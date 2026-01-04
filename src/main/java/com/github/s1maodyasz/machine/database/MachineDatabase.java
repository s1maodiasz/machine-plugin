package com.github.s1maodyasz.machine.database;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class MachineDatabase {

    private final Plugin plugin;

    private final MongoClient client;
    private final MachineRepository repository;
    private final MachineCache cache;
    private final ExecutorService executor;
    private final Duration operationTimeout;

    public MachineDatabase(@NotNull Plugin plugin) {
        this.plugin = plugin;

        final ConfigurationSection database = plugin.getConfig().getConfigurationSection("database");
        if (database == null) throw new IllegalArgumentException("Missing config section: database");

        final String url = database.getString("url");
        if (url == null || url.isBlank()) throw new IllegalArgumentException("Missing config: database.url");

        final CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        final MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(url))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .applyToConnectionPoolSettings(b -> b
                .minSize(2)
                .maxSize(10)
                .maxWaitTime(5, TimeUnit.SECONDS)
                .maxConnectionIdleTime(1, TimeUnit.MINUTES))
            .applyToSocketSettings(b -> b
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS))
            .codecRegistry(pojoCodecRegistry)
            .build();

        this.client = MongoClients.create(settings);

        final String databaseName = database.getString("databaseName", "machine_database");
        final String collectionName = database.getString("collectionName", "machine");

        // IMPORTANT: apply codec registry to the database/collection
        final var db = client.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);
        final var collection = db.getCollection(collectionName, Machine.class);

        this.repository = new MachineRepository(collection);
        this.cache = new MachineCache(plugin);

        this.executor = Executors.newFixedThreadPool(4);

        final long timeoutMillis = database.getLong("operationTimeoutMillis", 1500L);
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
            .whenComplete((r, ex) -> {
                if (ex != null) {
                    plugin.getLogger().severe("Mongo findAsync failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            })
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
            .whenComplete((r, ex) -> {
                if (ex != null) {
                    plugin.getLogger().severe("Mongo localizeAsync failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            })
            .exceptionally(ex -> Optional.empty());
    }

    public void saveSync(@NotNull Machine machine) {
        repository.upsert(machine);
        cache.put(machine);
    }

    public @NotNull CompletableFuture<Void> saveAsync(@NotNull Machine machine) {
        return withTimeout(CompletableFuture.runAsync(() -> saveSync(machine), executor))
            .whenComplete((v, ex) -> {
                if (ex != null) {
                    plugin.getLogger().severe("Mongo saveAsync failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            })
            .exceptionally(ex -> null);
    }

    public void removeSync(@NotNull Machine machine) {
        repository.deleteById(machine.getId());
        cache.remove(machine.getId());
    }

    public @NotNull CompletableFuture<Void> removeAsync(@NotNull Machine machine) {
        return withTimeout(CompletableFuture.runAsync(() -> removeSync(machine), executor))
            .whenComplete((v, ex) -> {
                if (ex != null) {
                    plugin.getLogger().severe("Mongo removeAsync failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            })
            .exceptionally(ex -> null);
    }

    public @NotNull CompletableFuture<Void> saveAllAsync(@NotNull List<Machine> machines) {
        return withTimeout(CompletableFuture.runAsync(() -> {
            if (machines.isEmpty()) return;
            repository.upsertAll(machines);
            for (Machine m : machines) cache.put(m);
        }, executor))
            .whenComplete((v, ex) -> {
                if (ex != null) {
                    plugin.getLogger().severe("Mongo saveAllAsync failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            })
            .exceptionally(ex -> null);
    }

    public @NotNull List<Machine> nearbySync(@NotNull MachineLocation center, int radius) {
        return cache.nearby(center, radius);
    }

    public @NotNull CompletableFuture<List<Machine>> nearbyAsync(@NotNull MachineLocation center, int radius) {
        return withTimeout(CompletableFuture.supplyAsync(() -> nearbySync(center, radius), executor))
            .whenComplete((r, ex) -> {
                if (ex != null) {
                    plugin.getLogger().severe("Mongo nearbyAsync failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            })
            .exceptionally(ex -> List.of());
    }

    public void close() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        client.close();
    }
}