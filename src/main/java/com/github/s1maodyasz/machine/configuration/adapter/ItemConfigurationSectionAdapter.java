package com.github.s1maodyasz.machine.configuration.adapter;

import com.github.s1maodyasz.machine.configuration.ItemConfiguration;
import com.github.s1maodyasz.machine.configuration.util.MaterialAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemConfigurationSectionAdapter {

    public static ItemConfiguration adapt(final ConfigurationSection section) {
        Objects.requireNonNull(section, "Something going wrong adapting configuration section of item.");

        final var materialName = section.getString("materialName", "STONE");
        final var material = MaterialAdapter.adapt(materialName);

        final var name = section.getString("name", null);
        final List<String> lore = section.getStringList("lore");

        final var customModelData = section.getInt("custom-model-data");
        final var modelId = section.getString("modelId", null);

        final var unbreakable = section.getBoolean("unbreakable", false);

        return ItemConfiguration.builder()
                .material(material)
                .name(name)
                .lore(lore)
                .customModelData(customModelData)
                .modelId(modelId)
                .unbreakable(unbreakable)
                .build();
    }
}