package com.github.s1maodyasz.machine.model;

import java.util.Map;

import com.github.s1maodyasz.machine.model.types.MachineUpgradeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class MachineData {

    @NonNull
    private final String key;

    private final Map<MachineUpgradeEnum, Integer> levels;

    private final double stack;
    private final double drops;
}
