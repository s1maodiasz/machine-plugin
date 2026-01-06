package com.github.s1maodyasz.machine.service.configuration;

import com.github.s1maodyasz.machine.service.configuration.loader.ConfigurationLoader;
import com.github.s1maodyasz.machine.service.configuration.model.BatteryConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class BatteryConfigurationRegistry extends ConfigurationRegistry<BatteryConfiguration> {

    public BatteryConfigurationRegistry(@NotNull Plugin plugin) {
        super(BatteryConfiguration::key, ConfigurationLoader.BATTERY_LOADER, plugin);
    }
}
