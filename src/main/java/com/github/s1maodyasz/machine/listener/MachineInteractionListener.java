package com.github.s1maodyasz.machine.listener;

import com.github.s1maodyasz.machine.configuration.ConfigurationManager;
import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.engine.MachineSnapshotUpdater;
import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineLocation;
import com.github.s1maodyasz.machine.panel.MainMachinePanel;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MachineInteractionListener implements Listener {

    private final Plugin plugin;
    private final NamespacedKey namespacedKey;
    private final MachineDatabase database;
    private final ConfigurationManager<MachineConfiguration> machineConfiguration;
    private final MachineSnapshotUpdater updater;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(@NotNull PlayerInteractEntityEvent event) {
        final var entity = event.getRightClicked();
        final var data = entity.getPersistentDataContainer();

        final String hasKey = data.get(namespacedKey, PersistentDataType.STRING);
        if (hasKey == null) return;

        event.setCancelled(true);

        final var player = event.getPlayer();
        final var loc = entity.getLocation();
        if (loc.getWorld() == null) return;

        final var machineLoc = new MachineLocation(loc.getWorld().getUID(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        database.localizeAsync(machineLoc).thenAccept(opt -> {
            if (opt.isEmpty()) {
                return;
            }

            final var machine = opt.get();
            final var cfg = machineConfiguration.get(machine.getKey());
            if (cfg == null) return;

            Bukkit.getScheduler().runTask(plugin, () -> {
                final var updated = updater.update(machine, cfg);
                final var panel = new MainMachinePanel(plugin, updated, cfg, database, updater);
                player.openInventory(panel.getInventory());
            });
        });
    }
}
