package com.github.s1maodyasz.machine.panel;

import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.service.refresher.MachineRefresher;
import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.configuration.model.MachineConfiguration;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public abstract class MachinePanel implements Panel {

    protected final Plugin plugin;
    protected final Machine machine;
    protected final MachineConfiguration configuration;
    protected final MachineDatabase database;
    protected final MachineRefresher updater;

}
