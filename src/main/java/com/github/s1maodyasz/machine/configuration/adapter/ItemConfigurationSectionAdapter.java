package com.github.s1maodyasz.machine.configuration.adapter;

import com.github.s1maodyasz.machine.model.ItemConfiguration;
import com.github.s1maodyasz.machine.model.TextConfiguration;
import java.util.ArrayList;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemConfigurationSectionAdapter {

  public static ItemConfiguration adapt(final ConfigurationSection section) {
    Objects.requireNonNull(section, "Item configuration section cannot be null.");

    final var value = section.getString("value", null);
    Objects.requireNonNull(value, "Item value cannot be null.");

    final var model = section.getString("model", null);

    final var rawName = section.getString("name", null);
    Objects.requireNonNull(rawName, "Item name cannot be null.");
    final var name = TextConfigurationValueAdapter.adapt(rawName);

    final var rawLore = section.getStringList("lore");
    final var lore = new ArrayList<TextConfiguration>();
    for (var s : rawLore) {
      final var line = TextConfigurationValueAdapter.adapt(s);
      lore.add(line);
    }

    final var unbreakable = section.getBoolean("unbreakable", false);
    return ItemConfiguration.builder()
        .value(value)
        .model(model)
        .name(name)
        .lore(lore)
        .unbreakable(unbreakable)
        .build();
  }
}
