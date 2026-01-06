package com.github.s1maodyasz.machine.service.configuration;

import com.github.s1maodyasz.machine.service.configuration.loader.ConfigurationLoader;
import com.github.s1maodyasz.machine.service.configuration.model.MachineConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class MachineConfigurationRegistry extends ConfigurationRegistry<MachineConfiguration> {

    public MachineConfigurationRegistry(@NotNull Plugin plugin) {
        super(MachineConfiguration::key, ConfigurationLoader.MACHINE_LOADER, plugin);

    }
}
