package com.github.s1maodyasz.machine.database;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class MachineCache {

    private static final int BIT_MASK_XZ = 0x3FFFFFF;
    private static final int BIT_MASK_Y = 0xFFF;

    private static final int SHIFT_Y = 0;
    private static final int SHIFT_Z = 12;
    private static final int SHIFT_X = 38;

    private final long maximumSize;
    private final long expireAfterAccessSeconds;

    private final Cache<UUID, Machine> byId;
    private final Cache<UUID, Cache<Long, UUID>> idByWorldAndPacked;

    public MachineCache(@NonNull Plugin plugin) {
        var config = plugin.getConfig();
        this.maximumSize = config.getLong("machine.cache.maximumSize", 100_000);
        this.expireAfterAccessSeconds = config.getLong("machine.cache.expireAfterAccessSeconds", 1800);

        this.byId = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(expireAfterAccessSeconds, TimeUnit.SECONDS)
                .build();

        this.idByWorldAndPacked = Caffeine.newBuilder()
                .maximumSize(64)
                .expireAfterAccess(expireAfterAccessSeconds, TimeUnit.SECONDS)
                .build();
    }

    public void put(@NotNull Machine machine) {
        final UUID id = machine.id();
        byId.put(id, machine);

        final MachineLocation loc = machine.location();
        final long packed = pack(loc.x(), loc.y(), loc.z());

        worldCache(loc.worldId()).put(packed, id);
    }

    public Optional<Machine> get(@NotNull UUID id) {
        return Optional.ofNullable(byId.getIfPresent(id));
    }

    public Optional<Machine> localize(@NotNull MachineLocation location) {
        final UUID id = worldCache(location.worldId()).getIfPresent(pack(location.x(), location.y(), location.z()));
        if (id == null) return Optional.empty();
        return Optional.ofNullable(byId.getIfPresent(id));
    }

    public void remove(@NotNull UUID id) {
        final Machine machine = byId.getIfPresent(id);
        if (machine != null) {
            final MachineLocation loc = machine.location();
            worldCache(loc.worldId()).invalidate(pack(loc.x(), loc.y(), loc.z()));
        }
        byId.invalidate(id);
    }

    public @NotNull List<Machine> nearby(@NotNull MachineLocation location, int radius) {
        if (radius < 0) return List.of();

        final UUID worldId = location.worldId();
        final Cache<Long, UUID> worldIndex = worldCache(worldId);

        final int cx = location.x();
        final int cy = location.y();
        final int cz = location.z();

        final int r2 = radius * radius;
        final List<Machine> result = new ArrayList<>();

        for (int x = cx - radius; x <= cx + radius; x++) {
            final int dx = x - cx;
            final int dx2 = dx * dx;

            for (int y = cy - radius; y <= cy + radius; y++) {
                final int dy = y - cy;
                final int dxy2 = dx2 + dy * dy;
                if (dxy2 > r2) continue;

                for (int z = cz - radius; z <= cz + radius; z++) {
                    final int dz = z - cz;
                    if (dxy2 + dz * dz > r2) continue;

                    final UUID id = worldIndex.getIfPresent(pack(x, y, z));
                    if (id == null) continue;

                    final Machine m = byId.getIfPresent(id);
                    if (m != null) result.add(m);
                }
            }
        }

        return result;
    }

    private Cache<Long, UUID> worldCache(UUID worldId) {
        Cache<Long, UUID> cache = idByWorldAndPacked.getIfPresent(worldId);
        if (cache != null) return cache;

        Cache<Long, UUID> created = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(expireAfterAccessSeconds, TimeUnit.SECONDS)
                .build();

        idByWorldAndPacked.put(worldId, created);
        return created;
    }

    private static long pack(int x, int y, int z) {
        final long lx = ((long) x) & BIT_MASK_XZ;
        final long lz = ((long) z) & BIT_MASK_XZ;
        final long ly = ((long) y) & BIT_MASK_Y;

        return (lx << SHIFT_X) | (lz << SHIFT_Z) | (ly << SHIFT_Y);
    }
}
