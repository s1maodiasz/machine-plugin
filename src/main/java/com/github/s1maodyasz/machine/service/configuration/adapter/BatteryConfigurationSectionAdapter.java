package com.github.s1maodyasz.machine.service.configuration.adapter;

import com.github.s1maodyasz.machine.service.configuration.model.BatteryConfiguration;
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

        final var key = section.getString("key");
        Objects.requireNonNull(key, "Battery configuration requires 'key'.");

        final var itemSection = section.getConfigurationSection("item");
        Objects.requireNonNull(itemSection, "Battery configuration requires 'item' section.");

        final var item = ItemConfigurationSectionAdapter.adapt(itemSection);
        Objects.requireNonNull(item, "Battery configuration item could not be adapted.");

        final var amount = section.getDouble("amount", 0);

        final var modifiersSection = section.getConfigurationSection("modifiers");
        Objects.requireNonNull(modifiersSection, "Battery modifiers configuration could not be adapted.");

        final var modifiers = new EnumMap<UpgradeEnum, Double>(UpgradeEnum.class);
        for (final var modifierKey : modifiersSection.getKeys(false)) {
            final var upgrade = UpgradeEnum.byValue(modifierKey);
            final var value = modifiersSection.getDouble(modifierKey, 0);
            modifiers.put(upgrade, value);
        }

        return BatteryConfiguration.builder()
            .key(key)
            .item(item)
            .amount(amount)
            .modifiers(modifiers)
            .build();
    }
}
