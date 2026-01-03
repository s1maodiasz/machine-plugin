package com.github.s1maodyasz.machine.model;

import com.github.s1maodyasz.machine.model.enums.MachineUpgradeEnum;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class MachineConfiguration {

    @NonNull
    private String key;

    @NonNull
    private String name;

    @Builder.Default
    private double price = 0;

    private double drop;

    @NonNull
    private ItemConfiguration item;

    /** The way the machine gonna be displayed for player (Can be a Block or a Model) */
    @NonNull
    private MachineDisplayConfiguration display;

    /** Modifier for individual level */
    @NonNull
    @Singular
    private Map<MachineUpgradeEnum, MachineUpgradeConfiguration> upgrades;
}
