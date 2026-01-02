package com.github.s1maodyasz.machine.service;

import com.github.s1maodyasz.machine.configuration.AbstractConfigurationManager;
import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.handlers.MachinePlacementResult;
import com.github.s1maodyasz.machine.handlers.PlacementMachineHandler;
import com.github.s1maodyasz.machine.item.ItemFactory;
import com.github.s1maodyasz.machine.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.model.BatteryData;
import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.provider.CustomEntityProvider;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@RequiredArgsConstructor
public final class MachineService {

    private final MachineDatabase database;
    private final AbstractConfigurationManager<MachineConfiguration> machineConfiguration;
    private final AbstractConfigurationManager<BatteryConfiguration> batteryConfiguration;
    private final CustomEntityProvider entityProvider;
    private final CustomItemProvider itemProvider;
    private final ItemFactory<MachineConfiguration, MachineData> machineFactory;
    private final ItemFactory<BatteryConfiguration, BatteryData> batteryFactory;
    private final PlacementMachineHandler handler;

    public GiveResult give(@NotNull GiveOptions options) {
        final var purpose = options.purpose;
        return switch (purpose) {
            case MACHINE -> give(
                    options.player,
                    options.key,
                    machineConfiguration,
                    machineFactory,
                    MachineData.builder().build());
            case BATTERY -> give(
                    options.player,
                    options.key,
                    batteryConfiguration,
                    batteryFactory,
                    BatteryData.builder().build());
        };
    }

    public MachinePlacementResult place(@NotNull Player player, @NotNull Location location, @NotNull BlockFace face, @NotNull ItemStack stack) {
        return handler.handle(player, location, stack);
    }

    @ApiStatus.Internal
    private <C, D> GiveResult give(
            @NotNull Player player,
            @NotNull String key,
            AbstractConfigurationManager<C> configuration,
            ItemFactory<C, D> factory,
            D data) {
        final var optional = configuration.get(key);
        if (optional.isEmpty()) return GiveResult.INVALID_KEY;

        final var config = optional.get();
        final var item = factory.create(config, data);

        player.give(item);
        return GiveResult.SUCCESS;
    }
}
