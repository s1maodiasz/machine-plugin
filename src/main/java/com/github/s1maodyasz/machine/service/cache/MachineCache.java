package com.github.s1maodyasz.machine.service.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class MachineCache {

    private final Cache<MachineLocation, Machine> cache;

    public MachineCache(@NonNull Plugin plugin) {
        var config = plugin.getConfig();

        var maximumSize = config.getLong("machine.cache.maximumSize", 100_000);
        var expireAfterAccessSeconds = config.getLong("machine.cache.expireAfterAccessSeconds", 1800);

        var builder = Caffeine.newBuilder()
            .maximumSize(maximumSize)
            .expireAfterAccess(expireAfterAccessSeconds, TimeUnit.SECONDS);

        this.cache = builder.build();
    }

    public void put(@NonNull Machine machine) {
        cache.put(machine.getLocation(), machine);
    }

    public Optional<Machine> getByLocation(@NonNull MachineLocation location) {
        return Optional.ofNullable(cache.getIfPresent(location));
    }

    public void removeByLocation(@NonNull MachineLocation location) {
        cache.invalidate(location);
    }

    public Map<MachineLocation, Machine> getAll() {
        return Map.copyOf(cache.asMap());
    }
}
