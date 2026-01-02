package com.github.s1maodyasz.machine.configuration.adapter;

import com.github.s1maodyasz.machine.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.model.ItemConfiguration;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BatteryConfigurationSectionAdapter {

    public static BatteryConfiguration adapt(ConfigurationSection section) {
        Objects.requireNonNull(section, "Battery configuration section is null.");

        final String key = section.getString("key", null);
        if (key == null || key.isBlank()) throw new IllegalArgumentException("Battery configuration requires 'key'.");

        final ConfigurationSection itemSection = section.getConfigurationSection("item");
        if (itemSection == null) throw new IllegalArgumentException("Battery configuration requires 'item' section.");

        final ItemConfiguration item = ItemConfigurationSectionAdapter.adapt(itemSection);
        if (item == null) throw new IllegalArgumentException("Battery configuration item could not be adapted.");

        final double amount = section.getDouble("amount", 0.0D);

        return BatteryConfiguration.builder().key(key).item(item).amount(amount).build();
    }
}
