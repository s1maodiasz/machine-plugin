package com.github.s1maodyasz.machine.model;

import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;
import java.util.EnumMap;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@Accessors(fluent = true)
public final class MachineData extends Data {

    @Singular
    private final Map<UpgradeEnum, Integer> levels;

    private final double drops;
}
