package com.github.s1maodyasz.machine.model;

import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class MachineConfiguration implements ItemConfigurable {

    @NonNull
    private String key;

    @NonNull
    private String name;

    @Builder.Default
    private double drop = 0;

    @NonNull
    private ItemConfiguration item;

    @NonNull
    private String model;

    /** Modifier for individual level */
    @NonNull
    @Singular
    private Map<UpgradeEnum, MachineUpgradeConfiguration> upgrades;
}
