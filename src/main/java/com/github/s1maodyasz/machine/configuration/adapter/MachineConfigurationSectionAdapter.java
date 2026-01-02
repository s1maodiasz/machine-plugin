package com.github.s1maodyasz.machine.configuration.adapter;

import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineUpgradeConfiguration;
import com.github.s1maodyasz.machine.model.types.MachineUpgradeEnum;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MachineConfigurationSectionAdapter {

    public static MachineConfiguration adapt(final ConfigurationSection section) {
        Objects.requireNonNull(section, "section");
        final var key = section.getName();

        final var name = section.getString("name", null);
        Objects.requireNonNull(name, "Machine name cannot be null. (key=" + key + ")");

        final var price = section.getDouble("price", 0D);

        final var itemSection = section.getConfigurationSection("item");
        Objects.requireNonNull(itemSection, "Machine item section cannot be null. (key=" + key + ")");
        final var item = ItemConfigurationSectionAdapter.adapt(itemSection);

        final var displaySection = section.getConfigurationSection("display");
        Objects.requireNonNull(displaySection, "Machine display section cannot be null. (key=" + key + ")");
        final var display = MachineDisplayConfigurationSectionAdapter.adapt(section);

        final Map<MachineUpgradeEnum, MachineUpgradeConfiguration> upgrades = new EnumMap<>(MachineUpgradeEnum.class);
        final var upgradesSection = section.getConfigurationSection("upgrades");
        if (upgradesSection == null) throw new IllegalStateException("Upgrade sections cannot be null");

        for (final String upgradeKey : upgradesSection.getKeys(false)) {
            final MachineUpgradeEnum upgrade = MachineUpgradeEnum.byValue(upgradeKey);

            final var upgradeSection = upgradesSection.getConfigurationSection(upgradeKey);
            Objects.requireNonNull(upgradeSection, "Upgrade section cannot be null.");

            final var upgradeConfiguration = MachineUpgradeConfigurationSectionAdapter.adapt(upgradeSection);
            upgrades.put(upgrade, upgradeConfiguration);
        }

        return MachineConfiguration.builder()
                .key(key)
                .name(name)
                .price(price)
                .item(item)
                .display(display)
                .upgrades(upgrades)
                .build();
    }
}
