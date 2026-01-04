package com.github.s1maodyasz.machine.configuration.adapter;

import com.github.s1maodyasz.machine.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;

import java.util.EnumMap;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BatteryConfigurationSectionAdapter {

    public static BatteryConfiguration adapt(ConfigurationSection section) {
        Objects.requireNonNull(section, "Battery configuration section is null.");

        final var key = Objects.requireNonNull(section.getString("key", null), "Battery configuration requires 'key'.");
        if (key.isBlank()) {
            throw new IllegalArgumentException("Battery configuration requires 'key'.");
        }

        final var itemSection = Objects.requireNonNull(section.getConfigurationSection("item"), "Battery configuration requires 'item' section.");
        final var item = Objects.requireNonNull(ItemConfigurationSectionAdapter.adapt(itemSection), "Battery configuration item could not be adapted.");

        final var amount = section.getDouble("amount", 0D);

        final var modifiersSection = Objects.requireNonNull(section.getConfigurationSection("modifiers"), "Battery modifiers configuration could not be adapted.");
        final var modifiers = new EnumMap<UpgradeEnum, Double>(UpgradeEnum.class);
        for (final var modifierSectionKey : modifiersSection.getKeys(false)) {
            final var upgrade = Objects.requireNonNull(UpgradeEnum.byValue(modifierSectionKey), "Unknown modifier upgrade: " + modifierSectionKey);

            final var value = modifiersSection.getDouble(modifierSectionKey, 0D);
            modifiers.put(upgrade, value);
        }

        return BatteryConfiguration.builder().key(key).item(item).amount(amount).modifiers(modifiers).build();
    }
}
