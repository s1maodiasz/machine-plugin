package com.github.s1maodyasz.machine.service.item.pipeline;

import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.model.TextConfiguration;
import com.github.s1maodyasz.machine.model.enums.MachineUpgradeEnum;
import com.github.s1maodyasz.machine.util.formatter.NumberFormatter;
import com.github.s1maodyasz.machine.util.formatter.TimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public final class MachinePlaceholderDecorator implements MachineItemDecorator {

    @Override
    public void decorate(@NotNull MachineItemContext ctx) {
        final MachineConfiguration configuration = ctx.configuration();
        final MachineData data = ctx.data();
        final var item = configuration.item();

        ctx.builder().name(apply(item.name(), configuration, data));

        final List<String> lore =
            item.lore().stream().map(line -> apply(line, configuration, data)).toList();

        ctx.builder().lore(lore);
    }

    private @NotNull String apply(
        @NotNull TextConfiguration text,
        @NotNull MachineConfiguration cfg,
        @NotNull MachineData data) {

        var s = text.content();
        if (s.isEmpty()) return "";

        final var placeholders = text.placeholders();
        if (placeholders.isEmpty()) return s;

        final var levels = data.levels();

        for (final var raw : placeholders) {
            if (raw == null || raw.isBlank()) continue;

            final var id = raw.trim().toLowerCase(Locale.ROOT);
            final var token = "{" + id + "}";

            s = s.replace(token, resolve(id, cfg, data, levels));
        }

        return s;
    }

    private @NotNull String resolve(
        @NotNull String id,
        @NotNull MachineConfiguration cfg,
        @NotNull MachineData data,
        @NotNull Map<MachineUpgradeEnum, Integer> levels) {

        return switch (id) {
            case "machine_key" -> cfg.key();
            case "machine_name" -> cfg.name();

            case "stored_energy" -> NumberFormatter.format(data.energy());
            case "stored_stack" -> NumberFormatter.format(data.stack());
            case "stored_drops" -> NumberFormatter.format(data.drops());

            case "capacity_level" -> String.valueOf(levels.getOrDefault(MachineUpgradeEnum.CAPACITY, 0));
            case "capacity_value" -> NumberFormatter.format(withUpgrade(cfg, MachineUpgradeEnum.CAPACITY, data.levels().get(MachineUpgradeEnum.CAPACITY)));

            case "efficiency_level" -> String.valueOf(levels.getOrDefault(MachineUpgradeEnum.EFFICIENCY, 0));
            case "efficiency_value" -> NumberFormatter.format(withUpgrade(cfg, MachineUpgradeEnum.EFFICIENCY, data.levels().get(MachineUpgradeEnum.EFFICIENCY)));

            case "consumption_level" -> String.valueOf(levels.getOrDefault(MachineUpgradeEnum.CONSUMPTION, 0));
            case "consumption_value" -> NumberFormatter.format(withUpgrade(cfg, MachineUpgradeEnum.CONSUMPTION, data.levels().get(MachineUpgradeEnum.CONSUMPTION)));

            case "speed_level" -> String.valueOf(levels.getOrDefault(MachineUpgradeEnum.SPEED, 0));
            case "speed_value" -> TimeFormatter.format((long) withUpgrade(cfg, MachineUpgradeEnum.SPEED, data.levels().get(MachineUpgradeEnum.SPEED)));

            default -> "{" + id + "}";
        };
    }

    private double withUpgrade(@NotNull MachineConfiguration cfg, @NotNull MachineUpgradeEnum upgrade, int level) {
        final var upgrades = cfg.upgrades();
        final var upgradeConfiguration = upgrades.get(upgrade);
        if (upgradeConfiguration == null)
            return 0D;
        return upgradeConfiguration.valueAt(level);
    }
}