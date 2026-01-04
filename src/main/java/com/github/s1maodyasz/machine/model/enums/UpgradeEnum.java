package com.github.s1maodyasz.machine.model.enums;

import org.jetbrains.annotations.NotNull;

public enum UpgradeEnum {
    OUTPUT_PER_CYCLE,
    ENERGY_CAPACITY,
    ENERGY_COST,
    CYCLE_SPEED,
    BATTERY_SLOTS;

    public static @NotNull UpgradeEnum byValue(@NotNull String value) {
        try {
            return UpgradeEnum.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid MachineUpgradeEnum: '" + value + "'", ex);
        }
    }
}
