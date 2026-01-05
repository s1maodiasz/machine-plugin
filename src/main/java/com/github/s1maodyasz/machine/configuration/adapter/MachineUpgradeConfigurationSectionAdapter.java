package com.github.s1maodyasz.machine.configuration.adapter;

import com.github.s1maodyasz.machine.configuration.model.MachineUpgradeConfiguration;
import com.github.s1maodyasz.machine.util.NumberUtil;

import java.util.Objects;
import java.util.TreeMap;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MachineUpgradeConfigurationSectionAdapter {

    public static MachineUpgradeConfiguration adapt(final ConfigurationSection section) {
        Objects.requireNonNull(section, "section");

        final var levelsSection = Objects.requireNonNull(section.getConfigurationSection("levels"), "Upgrade 'levels' section cannot be null.");

        final var levels = new TreeMap<Integer, MachineUpgradeConfiguration.Level>();

        for (final String levelKey : levelsSection.getKeys(false)) {
            final int level = NumberUtil.requireNotZero(levelKey, "Level value needs to be a number.");
            if (level <= 0) {
                throw new IllegalArgumentException("Upgrade level must be > 0 (found " + level + ")");
            }

            final ConfigurationSection levelSection = levelsSection.getConfigurationSection(levelKey);
            if (levelSection == null) {
                throw new IllegalArgumentException("Invalid upgrade level format at '" + levelsSection.getCurrentPath() + "." + levelKey + "'. Expected a section with {value, price}.");
            }

            final double value = levelSection.getDouble("value");
            final double price = levelSection.getDouble("price");

            levels.put(level, new MachineUpgradeConfiguration.Level(value, price));
        }

        if (levels.isEmpty())
            throw new IllegalStateException("Upgrade 'levels' section cannot be empty.");

        return MachineUpgradeConfiguration.builder().levels(levels).build();
    }
}