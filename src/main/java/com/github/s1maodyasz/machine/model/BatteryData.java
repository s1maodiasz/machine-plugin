package com.github.s1maodyasz.machine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class BatteryData extends StackableData {

    @NotNull
    private String key;

    private double energy;

}
