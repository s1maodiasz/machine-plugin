package com.github.s1maodyasz.machine.configuration.adapter;

import com.github.s1maodyasz.machine.model.ItemConfiguration;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemConfigurationSectionAdapter {

    public static ItemConfiguration adapt(final ConfigurationSection section) {
        Objects.requireNonNull(section, "Item configuration section cannot be null.");

        final var material = section.getString("material", null);
        Objects.requireNonNull(material, "Item value cannot be null.");

        final var url = section.getString("url", null);
        final var model = section.getString("model", null);

        final var name = section.getString("name", null);
        Objects.requireNonNull(name, "Item name cannot be null.");

        final var lore = section.getStringList("lore");

        final var unbreakable = section.getBoolean("unbreakable", false);
        return ItemConfiguration.builder()
                .material(material)
                .url(url)
                .model(model)
                .name(name)
                .lore(lore)
                .unbreakable(unbreakable)
                .build();
    }
}
