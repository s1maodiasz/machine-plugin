package com.github.s1maodyasz.machine.service.configuration.loader;

import com.github.s1maodyasz.machine.service.configuration.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.service.configuration.model.MachineConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ConfigurationLoader<T> {

    Machine MACHINE_LOADER = new Machine();
    Battery BATTERY_LOADER = new Battery();

    List<T> load(@NotNull FileConfiguration configuration);

    default List<T> load(@NotNull Plugin plugin) {
        final var configuration = plugin.getConfig();
        return load(configuration);
    }

    final class Machine implements ConfigurationLoader<MachineConfiguration> {
        @Override
        public List<MachineConfiguration> load(@NotNull FileConfiguration configuration) {
            return List.of();
        }
    }

    final class Battery implements ConfigurationLoader<BatteryConfiguration> {
        @Override
        public List<BatteryConfiguration> load(@NotNull FileConfiguration configuration) {
            return List.of();
        }
    }
}
