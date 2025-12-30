package com.github.s1maodyasz.machine.service.database;

import com.github.s1maodyasz.machine.model.Machine;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public final class MachineDatabase {

    private final MongoClient client;
    private final MachineRepository repository;

    public MachineDatabase(Plugin plugin) {
        var mongoConfiguration = plugin.getConfig().getConfigurationSection("mongo");
        if (mongoConfiguration == null) throw new IllegalArgumentException("Configuration is not defined.");

        var url = mongoConfiguration.getString("url");
        if (url == null) throw new IllegalArgumentException("Mongo url in configuration is null `database.url`.");

        var settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(url))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .applyToConnectionPoolSettings(builder -> builder
                .minSize(2)
                .maxSize(10)
                .maxWaitTime(5, TimeUnit.SECONDS)
                .maxConnectionIdleTime(1, TimeUnit.MINUTES))
            .applyToSocketSettings(builder -> builder
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS))
            .codecRegistry(CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
            ))
            .build();

        this.client = MongoClients.create(settings);

        var databaseName = mongoConfiguration.getString("databaseName");
        var database = client.getDatabase(databaseName == null ? "machine_database" : databaseName);

        var collectionName = mongoConfiguration.getString("collectionName");
        var collection = database.getCollection(collectionName == null ? "machine" : collectionName, Machine.class);

        this.repository = new MachineRepository(collection);
    }

    public MachineRepository repository() {
        return repository;
    }

    public void close() {
        client.close();
    }
}
