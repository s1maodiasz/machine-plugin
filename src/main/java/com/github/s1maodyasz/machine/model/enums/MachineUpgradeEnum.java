package com.github.s1maodyasz.machine.model.enums;

import org.jetbrains.annotations.NotNull;

public enum MachineUpgradeEnum {
    EFFICIENCY,
    CAPACITY,
    CONSUMPTION,
    SPEED;

    public static @NotNull MachineUpgradeEnum byValue(@NotNull String value) {
        try {
            return MachineUpgradeEnum.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid MachineUpgradeEnum: '" + value + "'", ex);
        }
    }
}
