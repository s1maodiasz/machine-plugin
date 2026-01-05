package com.github.s1maodyasz.machine.service;

import com.github.s1maodyasz.machine.configuration.ConfigurationManager;
import com.github.s1maodyasz.machine.configuration.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.configuration.model.MachineConfiguration;
import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.model.BatterySerializeData;
import com.github.s1maodyasz.machine.model.MachineSerializeData;
import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;
import com.github.s1maodyasz.machine.service.item.ItemFactory;
import com.github.s1maodyasz.machine.service.refresher.MachineRefresher;
import com.github.s1maodyasz.machine.util.PlayerInventoryUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MachineService {

    private final MachineDatabase database;
    private final MachineRefresher refresher;
    private final ConfigurationManager<MachineConfiguration> machineConfiguration;
    private final ConfigurationManager<BatteryConfiguration> batteryConfiguration;
    private final ItemFactory<MachineConfiguration, MachineSerializeData> machineFactory;
    private final ItemFactory<BatteryConfiguration, BatterySerializeData> batteryFactory;

    public IssueResult issueMachine(@NotNull Player player, @NotNull String key, int amount) {
        final var configuration = machineConfiguration.get(key);
        if (configuration == null)
            return IssueResult.INVALID_KEY;

        final var data = MachineSerializeData.builder()
            .level(UpgradeEnum.OUTPUT_PER_CYCLE, 0)
            .level(UpgradeEnum.ENERGY_COST, 0)
            .level(UpgradeEnum.CYCLE_SPEED, 0)
            .level(UpgradeEnum.BATTERY_SLOTS, 0)
            .level(UpgradeEnum.ENERGY_CAPACITY, 0)
            .build();

        final var stack = machineFactory.builderOf(player, configuration, data).amount(amount).build();
        final boolean added = PlayerInventoryUtil.give(player, stack);
        if (added)
            return IssueResult.SUCCESS;

        return IssueResult.INVENTORY_IS_FULL;
    }

    public IssueResult issueBattery(@NotNull Player player, @NotNull String key, int amount) {
        final var configuration = batteryConfiguration.get(key);
        if (configuration == null)
            return IssueResult.INVALID_KEY;

        final var data = BatterySerializeData.builder().build();

        final var stack = batteryFactory.builderOf(player, configuration, data).amount(amount).build();
        final boolean added = PlayerInventoryUtil.give(player, stack);
        if (added)
            return IssueResult.SUCCESS;

        return IssueResult.INVENTORY_IS_FULL;
    }
}
