package com.github.s1maodyasz.machine.configuration.adapter;

import com.github.s1maodyasz.machine.configuration.MachineConfiguration;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class MachineConfigurationAdapter {

    public static List<MachineConfiguration> withFileConfiguration(@NotNull ConfigurationSection configuration) {

    }

    public static MachineConfiguration withConfigurationSection(@NotNull ConfigurationSection section) {
        final var key = section.getName();
        final var name = section.getString("name");
        final var price = section.getDouble("price");
        final var item =
    }
}
