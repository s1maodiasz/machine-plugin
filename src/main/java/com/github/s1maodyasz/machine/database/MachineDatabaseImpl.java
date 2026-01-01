package com.github.s1maodyasz.machine.database;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class MachineDatabaseImpl {

	private final MongoClient client;
	private final MachineRepository repository;
	private final MachineCache cache;
	private final ExecutorService executor;

	public MachineDatabaseImpl(Plugin plugin) {
		var mongoConfiguration = plugin.getConfig().getConfigurationSection("mongo");
		if (mongoConfiguration == null)
			throw new IllegalArgumentException("Configuration is not defined.");

		var url = mongoConfiguration.getString("url");
		if (url == null)
			throw new IllegalArgumentException("Mongo url in configuration is null `database.url`.");

		var settings =
				MongoClientSettings.builder()
						.applyConnectionString(new ConnectionString(url))
						.uuidRepresentation(UuidRepresentation.STANDARD)
						.applyToConnectionPoolSettings(
								builder ->
										builder
												.minSize(2)
												.maxSize(10)
												.maxWaitTime(5, TimeUnit.SECONDS)
												.maxConnectionIdleTime(1, TimeUnit.MINUTES))
						.applyToSocketSettings(
								builder ->
										builder.connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS))
						.codecRegistry(
								CodecRegistries.fromRegistries(
										MongoClientSettings.getDefaultCodecRegistry(),
										CodecRegistries.fromProviders(
												PojoCodecProvider.builder().automatic(true).build())))
						.build();

		this.client = MongoClients.create(settings);

		var databaseName = mongoConfiguration.getString("databaseName");
		var database = client.getDatabase(databaseName == null ? "machine_database" : databaseName);

		var collectionName = mongoConfiguration.getString("collectionName");
		var collection =
				database.getCollection(collectionName == null ? "machine" : collectionName, Machine.class);

		this.repository = new MachineRepositoryImpl(collection);
		this.cache = new MachineCacheImpl(plugin);

		this.executor = Executors.newFixedThreadPool(4);
	}

	public @NotNull Optional<Machine> findByLocationSync(@NotNull MachineLocation location) {
		return findByLocationAsync(location).join();
	}

	public @NotNull CompletableFuture<Optional<Machine>> findByLocationAsync(
			@NotNull MachineLocation location) {
		final Optional<Machine> cached = cache.getByLocation(location);
		if (cached.isPresent()) {
			return CompletableFuture.completedFuture(cached);
		}

		return CompletableFuture.supplyAsync(() -> repository.findByLocation(location), executor)
				.orTimeout(5, TimeUnit.SECONDS)
				.exceptionally(e -> Optional.empty())
				.thenApply(
						machine -> {
							machine.ifPresent(cache::put);
							return machine;
						});
	}

	public void saveSync(@NotNull Machine machine) {
		saveAsync(machine).join();
	}

	public @NotNull CompletableFuture<Void> saveAsync(@NotNull Machine machine) {
		cache.put(machine);

		return CompletableFuture.runAsync(() -> repository.save(machine), executor)
				.orTimeout(5, TimeUnit.SECONDS)
				.exceptionally(e -> null);
	}

	public void removeSync(@NotNull Machine machine) {
		removeAsync(machine).join();
	}

	public @NotNull CompletableFuture<Void> removeAsync(@NotNull Machine machine) {
		cache.removeByLocation(machine.getLocation());

		return CompletableFuture.runAsync(() -> repository.deleteById(machine.getId()), executor)
				.orTimeout(5, TimeUnit.SECONDS)
				.exceptionally(e -> null);
	}

	public void shutdown() {
		client.close();
		executor.shutdown();
	}
}
