package com.github.s1maodyasz.machine.item;

import com.github.s1maodyasz.machine.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.model.BatteryData;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import com.github.s1maodyasz.machine.util.ItemBuilder;
import com.github.s1maodyasz.machine.util.formatter.NumberFormatter;
import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class BatteryItemFactory extends ItemFactory<BatteryConfiguration, BatteryData> {

  public BatteryItemFactory(
      @NotNull NamespacedKey key, @NotNull CustomItemProvider provider, @NotNull Gson gson) {
    super(key, provider, gson);
  }

  @Override
  public @NotNull ItemStack create(@NotNull BatteryConfiguration cfg, @NotNull BatteryData data) {
    final var builder = ItemBuilder.of();
    final var item = cfg.item();

    final var type = item.value();
    final var model = item.model();

    if (model == null || model.isBlank()) {
      builder.type(type);
    } else {
      builder.stack(provider.resolve(model));
    }

    final var name = apply(item.name(), cfg, data);
    builder.name(name);

    final var lore = item.lore().stream().map(line -> apply(line, cfg, data)).toList();
    builder.lore(lore);

    builder.unbreakable(item.unbreakable());

    final var payload = gson.toJson(data);
    builder.pdc().string(key, payload);

    return builder.build();
  }

  @Override
  String resolve(String placeholder, BatteryConfiguration configuration, BatteryData data) {
    return switch (placeholder) {
      case "battery_stack" -> NumberFormatter.format(data.stack());
      case "battery_total" -> NumberFormatter.format(configuration.amount() * data.stack());
      default -> "{" + placeholder + "}";
    };
  }
}
