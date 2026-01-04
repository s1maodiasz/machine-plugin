package com.github.s1maodyasz.machine.model;

import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;

import java.util.EnumMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class MachineData extends StackableData {

    @NonNull
    private final String key;

    @NonNull
    @Builder.Default
    private final Map<UpgradeEnum, Integer> levels = new EnumMap<>(UpgradeEnum.class);

    @Builder.Default
    private final double drops = 0;
}
