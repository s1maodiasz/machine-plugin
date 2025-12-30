package com.github.s1maodyasz.machine.service;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import com.github.s1maodyasz.machine.service.cache.MachineCache;
import com.github.s1maodyasz.machine.service.database.MachineDatabase;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class MachineService {

    private final MachineCache cache;
    private final MachineDatabase database;
    private final ExecutorService executor;

    public MachineService(Plugin plugin) {
        this.cache = new MachineCache(plugin);
        this.database = new MachineDatabase(plugin);
        this.executor = Executors.newFixedThreadPool(4); // For general proposes is affordable, otherwise make configurable
    }

    public Optional<Machine> findByLocation(MachineLocation location) {
        var cached = cache.getByLocation(location);
        if (cached.isPresent())
            return cached;

        return CompletableFuture
            .supplyAsync(() -> database.repository().findByLocation(location), executor)
            .orTimeout(5, TimeUnit.SECONDS)
            .exceptionally(e -> Optional.empty())
            .thenApply(machine -> {
                machine.ifPresent(cache::put);
                return machine;
            })
            .join();
    }

    public CompletableFuture<Void> save(Machine machine) {
        cache.put(machine);

        return CompletableFuture
            .runAsync(() -> database.repository().merge(machine), executor)
            .orTimeout(5, TimeUnit.SECONDS)
            .exceptionally(e -> null);
    }

    public CompletableFuture<Void> remove(Machine machine) {
        final var loc = machine.getLocation();
        cache.removeByLocation(loc);

        return CompletableFuture
            .runAsync(() -> database.repository().deleteById(machine.getId()), executor)
            .orTimeout(5, TimeUnit.SECONDS)
            .exceptionally(e -> null);
    }

    public void shutdown() {
        database.close();
        executor.shutdown();
    }
}
