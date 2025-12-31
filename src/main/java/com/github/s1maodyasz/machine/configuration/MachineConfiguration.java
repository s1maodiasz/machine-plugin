package com.github.s1maodyasz.machine.configuration;

import com.github.s1maodyasz.machine.model.enums.MachineUpgradeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
@Builder(toBuilder = true)
public final class MachineConfiguration {

    @NonNull
    private String key;

    @NonNull
    private String name;

    @Builder.Default
    private double price = 0;

    @NonNull
    private ItemConfiguration item;

    /**
     * The way the machine gonna be displayed for player (Can be a Block or a Model)
     */
    @NonNull
    private MachineDisplayConfiguration display;

    private double capacity;
    private double efficiency;
    private double consumption;

    /**
     * Modifier for individual level
     */
    @NonNull
    @Singular
    private Map<MachineUpgradeEnum, Double> upgrades;

}
