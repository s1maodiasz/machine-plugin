package com.github.s1maodyasz.machine.configuration.adapter;

import com.github.s1maodyasz.machine.model.MachineUpgradeConfiguration;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import com.github.s1maodyasz.machine.util.NumberParseUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MachineUpgradeConfigurationSectionAdapter {

    public static MachineUpgradeConfiguration adapt(final ConfigurationSection section) {
        Objects.requireNonNull(section, "section");

        final var levelsSection = Objects.requireNonNull(section.getConfigurationSection("levels"), "Upgrade 'levels' section cannot be null.");

        final Map<Integer, Double> levels = new TreeMap<>();

        for (final String levelKey : levelsSection.getKeys(false)) {
            final int level = NumberParseUtil.requireNotZero(levelKey, "Level value needs to be a number.");

            if (level <= 0) throw new IllegalArgumentException("Upgrade level must be > 0 (found " + level + ")");

            final double value = levelsSection.getDouble(levelKey);
            levels.put(level, value);
        }

        if (levels.isEmpty()) throw new IllegalStateException("Upgrade 'levels' section cannot be empty.");

        return MachineUpgradeConfiguration.builder().levels(levels).build();
    }
}