package com.github.s1maodyasz.machine.configuration.adapter;

import com.github.s1maodyasz.machine.configuration.model.MachineConfiguration;
import com.github.s1maodyasz.machine.configuration.model.MachineUpgradeConfiguration;
import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;
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
        final var drop = section.getDouble("drop", 0D);

        final var itemSection = section.getConfigurationSection("item");
        Objects.requireNonNull(itemSection, "Machine item section cannot be null. (key=" + key + ")");
        final var item = ItemConfigurationSectionAdapter.adapt(itemSection);

        final var model = section.getString("model");
        Objects.requireNonNull(model, "Machine model cannot be null. (key=" + key + ")");

        final Map<UpgradeEnum, MachineUpgradeConfiguration> upgrades = new EnumMap<>(UpgradeEnum.class);
        final var upgradesSection = section.getConfigurationSection("upgrades");
        if (upgradesSection == null) throw new IllegalStateException("Upgrade sections cannot be null");

        for (final String upgradeKey : upgradesSection.getKeys(false)) {
            final UpgradeEnum upgrade = UpgradeEnum.byValue(upgradeKey);

            final var upgradeSection = upgradesSection.getConfigurationSection(upgradeKey);
            Objects.requireNonNull(upgradeSection, "Upgrade section cannot be null.");

            final var upgradeConfiguration = MachineUpgradeConfigurationSectionAdapter.adapt(upgradeSection);
            upgrades.put(upgrade, upgradeConfiguration);
        }

        return MachineConfiguration.builder()
                .key(key)
                .name(name)
                .drop(drop)
                .item(item)
                .model(model)
                .upgrades(upgrades)
                .build();
    }
}
