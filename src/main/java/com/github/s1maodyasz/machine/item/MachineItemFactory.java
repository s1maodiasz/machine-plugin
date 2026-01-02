package com.github.s1maodyasz.machine.item;

import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.model.MachineUpgradeEnum;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import com.github.s1maodyasz.machine.util.ItemBuilder;
import com.github.s1maodyasz.machine.util.formatter.NumberFormatter;
import com.github.s1maodyasz.machine.util.formatter.TimeFormatter;
import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class MachineItemFactory extends ItemFactory<MachineConfiguration, MachineData> {

  public MachineItemFactory(
      @NotNull NamespacedKey key, @NotNull CustomItemProvider provider, @NotNull Gson gson) {
    super(key, provider, gson);
  }

  @Override
  public @NotNull ItemStack create(
      @NotNull MachineConfiguration configuration, @NotNull MachineData data) {
    final var builder = ItemBuilder.of();
    final var item = configuration.item();

    final String type = item.value();
    final String model = item.model();

    if (model == null || model.isBlank()) builder.type(type);
    else builder.stack(provider.resolve(model));

    final var nameConfiguration = item.name();
    final var name = apply(nameConfiguration, configuration, data);
    builder.name(name);

    final var lore = item.lore().stream().map(line -> apply(line, configuration, data)).toList();
    builder.lore(lore);

    final var payload = gson.toJson(data);
    builder.pdc().set(key, PersistentDataType.STRING, payload);

    return builder.build();
  }

  @Override
  String resolve(@NotNull String id, @NotNull MachineConfiguration cfg, @NotNull MachineData data) {
    final var levels = data.levels();
    return switch (id) {
      case "machine_key" -> cfg.key();
      case "machine_name" -> cfg.name();

      case "stored_stack" -> NumberFormatter.format(data.stack());
      case "stored_drops" -> NumberFormatter.format(data.drops());

      case "capacity_level" -> String.valueOf(levels.getOrDefault(MachineUpgradeEnum.CAPACITY, 0));
      case "capacity_value" -> NumberFormatter.format(
          withUpgrade(
              cfg, MachineUpgradeEnum.CAPACITY, data.levels().get(MachineUpgradeEnum.CAPACITY)));

      case "efficiency_level" -> String.valueOf(
          levels.getOrDefault(MachineUpgradeEnum.EFFICIENCY, 0));
      case "efficiency_value" -> NumberFormatter.format(
          withUpgrade(
              cfg,
              MachineUpgradeEnum.EFFICIENCY,
              data.levels().get(MachineUpgradeEnum.EFFICIENCY)));

      case "consumption_level" -> String.valueOf(
          levels.getOrDefault(MachineUpgradeEnum.CONSUMPTION, 0));
      case "consumption_value" -> NumberFormatter.format(
          withUpgrade(
              cfg,
              MachineUpgradeEnum.CONSUMPTION,
              data.levels().get(MachineUpgradeEnum.CONSUMPTION)));

      case "speed_level" -> String.valueOf(levels.getOrDefault(MachineUpgradeEnum.SPEED, 0));
      case "speed_value" -> TimeFormatter.format(
          (long)
              withUpgrade(
                  cfg, MachineUpgradeEnum.SPEED, data.levels().get(MachineUpgradeEnum.SPEED)));

      default -> "{" + id + "}";
    };
  }

  private double withUpgrade(
      @NotNull MachineConfiguration cfg, @NotNull MachineUpgradeEnum upgrade, int level) {
    final var upgrades = cfg.upgrades();
    final var upgradeConfiguration = upgrades.get(upgrade);
    if (upgradeConfiguration == null) return 0D;
    return upgradeConfiguration.valueAt(level);
  }
}
