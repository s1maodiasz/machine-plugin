package com.github.s1maodyasz.machine.model;

import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class BatteryConfiguration implements ItemConfigurable {

    @NonNull
    private String key;

    @NonNull
    private ItemConfiguration item;

    private double amount;

    @Singular
    private final Map<UpgradeEnum, Double> modifiers;

}
