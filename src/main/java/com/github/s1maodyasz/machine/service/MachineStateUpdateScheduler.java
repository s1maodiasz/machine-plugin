package com.github.s1maodyasz.machine.service;

import com.github.s1maodyasz.machine.database.MachineDatabase;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

/**
 * Periodically flushes dirty machine states from cache to the database.
 *
 * Expected config section (example path: "persistence"):
 *  enabled: true
 *  interval-seconds: 60
 *  max-batch-size: 1000
 */
public final class MachineStateUpdateScheduler implements AutoCloseable {

    private final Plugin plugin;
    private final MachineDatabase database;
    private final MachineStateCache cache;

    private final boolean enabled;
    private final int intervalSeconds;
    private final int maxBatchSize;

    private BukkitTask task;

    public MachineStateUpdateScheduler(
        @NotNull Plugin plugin,
        @NotNull MachineDatabase database,
        @NotNull MachineStateCache cache
    ) {
        this.plugin = plugin;
        this.database = database;
        this.cache = cache;

        final ConfigurationSection sec =
            plugin.getConfig().getConfigurationSection("persistence");

        this.enabled = sec == null || sec.getBoolean("enabled", true);
        this.intervalSeconds = sec == null ? 60 : Math.max(1, sec.getInt("interval-seconds", 60));
        this.maxBatchSize = sec == null ? 1000 : Math.max(1, sec.getInt("max-batch-size", 1000));
    }

    public void start() {
        if (!enabled || task != null) return;

        final long ticks = Math.max(1L, TimeUnit.SECONDS.toMillis(intervalSeconds) / 50L);

        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                flushOnce();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }, ticks, ticks);
    }

    private void flushOnce() {
        final var ids = cache.drain();
        if (ids.isEmpty())
            return;

        int i = 0;
        for (final var id : ids) {
            i++;
            if (i >= maxBatchSize) break;
        }
    }

    public void stop() {
        if (task != null) task.cancel();
        task = null;
    }

    @Override
    public void close() {
        stop();
    }
}