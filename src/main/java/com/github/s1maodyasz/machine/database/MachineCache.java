package com.github.s1maodyasz.machine.database;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class MachineCache {

    private final Cache<UUID, Machine> byId;
    private final Cache<MachineLocation, UUID> idByLocation;

    public MachineCache(@NonNull Plugin plugin) {
        var config = plugin.getConfig();

        var maximumSize = config.getLong("machine.cache.maximumSize", 100_000);
        var expireAfterAccessSeconds = config.getLong("machine.cache.expireAfterAccessSeconds", 1800);

        this.byId = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(expireAfterAccessSeconds, TimeUnit.SECONDS)
                .build();

        this.idByLocation = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(expireAfterAccessSeconds, TimeUnit.SECONDS)
                .build();
    }

    public void put(@NotNull Machine machine) {
        byId.put(machine.id(), machine);
        idByLocation.put(machine.location(), machine.id());
    }

    public Optional<Machine> get(@NotNull UUID id) {
        return Optional.ofNullable(byId.getIfPresent(id));
    }

    public Optional<Machine> localize(@NotNull MachineLocation location) {
        final UUID id = idByLocation.getIfPresent(location);
        if (id == null) return Optional.empty();
        return Optional.ofNullable(byId.getIfPresent(id));
    }

    public void remove(@NotNull UUID id) {
        final var machine = byId.getIfPresent(id);
        if (machine != null) {
            final var location = machine.location();
            idByLocation.invalidate(location);
        }
        byId.invalidate(id);
    }
}
