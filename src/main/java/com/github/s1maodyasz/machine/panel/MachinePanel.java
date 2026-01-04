package com.github.s1maodyasz.machine.panel;

import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.engine.MachineSnapshotUpdater;
import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineConfiguration;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public abstract class MachinePanel implements Panel {

    protected Panel previous;

    protected final Plugin plugin;
    protected final Machine machine;
    protected final MachineConfiguration configuration;
    protected final MachineDatabase database;
    protected final MachineSnapshotUpdater updater;

    protected final Inventory inventory = Bukkit.createInventory(this, 27, Component.text("Painel Principal"));
}
