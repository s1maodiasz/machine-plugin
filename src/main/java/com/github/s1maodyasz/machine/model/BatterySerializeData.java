package com.github.s1maodyasz.machine.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

@Getter
@SuperBuilder(toBuilder = true)
@Accessors(fluent = true)
public final class BatterySerializeData extends SerializeData {

    @NotNull
    private String key;

    private double energy;
}
