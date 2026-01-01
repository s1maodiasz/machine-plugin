package com.github.s1maodyasz.machine.database;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class MachineCacheImpl implements MachineCache {

	private final Cache<MachineLocation, Machine> cache;

	public MachineCacheImpl(@NonNull Plugin plugin) {
		var config = plugin.getConfig();

		var maximumSize = config.getLong("machine.cache.maximumSize", 100_000);
		var expireAfterAccessSeconds = config.getLong("machine.cache.expireAfterAccessSeconds", 1800);

		this.cache =
				Caffeine.newBuilder()
						.maximumSize(maximumSize)
						.expireAfterAccess(expireAfterAccessSeconds, TimeUnit.SECONDS)
						.build();
	}

	@Override
	public void put(@NotNull Machine machine) {
		cache.put(machine.getLocation(), machine);
	}

	@Override
	public Optional<Machine> getByLocation(@NotNull MachineLocation location) {
		return Optional.ofNullable(cache.getIfPresent(location));
	}

	@Override
	public void removeByLocation(@NotNull MachineLocation location) {
		cache.invalidate(location);
	}

	@Override
	public @NotNull Map<MachineLocation, Machine> getAll() {
		return Map.copyOf(cache.asMap());
	}
}
