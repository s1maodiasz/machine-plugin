package com.github.s1maodyasz.machine.database;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class MachineDatabaseImpl implements MachineDatabase {

    private final MongoClient client;
    private final MachineRepository repository;
    private final MachineCache cache;
    private final ExecutorService executor;

    public MachineDatabaseImpl(@NotNull Plugin plugin) {
        final ConfigurationSection mongo = plugin.getConfig().getConfigurationSection("mongo");
        if (mongo == null) throw new IllegalArgumentException("Missing config section: mongo");

        final String url = mongo.getString("url");
        if (url == null || url.isBlank()) throw new IllegalArgumentException("Missing config: mongo.url");

        final MongoClientSettings settings =
            MongoClientSettings.builder()
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
                .codecRegistry(CodecRegistries.fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
                ))
                .build();

        this.client = MongoClients.create(settings);

        final String databaseName = Optional.ofNullable(mongo.getString("databaseName"))
            .filter(s -> !s.isBlank())
            .orElse("machine_database");

        final String collectionName = Optional.ofNullable(mongo.getString("collectionName"))
            .filter(s -> !s.isBlank())
            .orElse("machine");

        final var collection = client.getDatabase(databaseName).getCollection(collectionName, Machine.class);

        this.repository = new MachineRepositoryImpl(collection);
        this.cache = new MachineCacheImpl(plugin);

        final ThreadFactory tf = r -> {
            final Thread t = new Thread(r, "machine-db");
            t.setDaemon(true);
            return t;
        };
        this.executor = Executors.newFixedThreadPool(4, tf);
    }

    @Override
    public @NotNull Optional<Machine> findByLocationSync(@NotNull MachineLocation location) {
        return findByLocationAsync(location).join();
    }

    @Override