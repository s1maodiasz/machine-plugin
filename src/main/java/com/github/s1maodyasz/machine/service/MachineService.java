package com.github.s1maodyasz.machine.service;

import com.github.s1maodyasz.machine.service.configuration.ConfigurationRegistries;
import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.model.BatteryData;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;
import com.github.s1maodyasz.machine.service.item.ItemFactories;
import com.github.s1maodyasz.machine.service.state.MachineStateResolver;
import com.github.s1maodyasz.machine.util.PlayerInventoryUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MachineService {

    private final MachineDatabase database;
    private final MachineStateResolver resolver;
    private final ConfigurationRegistries registries;
    private final ItemFactories factories;

    public IssueResult issueMachine(@NotNull Player player, @NotNull String key, int amount) {
        final var configuration = registries.machines().get(key);
        if (configuration == null)
            return IssueResult.INVALID_KEY;

        final var data = MachineData.builder()
            .key(key)
            .level(UpgradeEnum.OUTPUT_PER_CYCLE, 0)
            .level(UpgradeEnum.ENERGY_COST, 0)
            .level(UpgradeEnum.CYCLE_SPEED, 0)
            .level(UpgradeEnum.BATTERY_SLOTS, 0)
            .level(UpgradeEnum.ENERGY_CAPACITY, 0)
            .build();

        final var stack = factories.machines().builderOf(player, configuration, data).amount(amount).build();
        final boolean added = PlayerInventoryUtil.give(player, stack);
        if (added)
            return IssueResult.SUCCESS;
        return IssueResult.INVENTORY_IS_FULL;
    }

    public IssueResult issueBattery(@NotNull Player player, @NotNull String key, int amount) {
        final var configuration = registries.batteries().get(key);
        if (configuration == null)
            return IssueResult.INVALID_KEY;

        final var data = BatteryData
            .builder()
            .key(key)
            .build();

        final var stack = factories.batteries().builderOf(player, configuration, data).amount(amount).build();
        final boolean added = PlayerInventoryUtil.give(player, stack);
        if (added)
            return IssueResult.SUCCESS;
        return IssueResult.INVENTORY_IS_FULL;
    }

    public void shutdown() {
        database.close();
    }
}
