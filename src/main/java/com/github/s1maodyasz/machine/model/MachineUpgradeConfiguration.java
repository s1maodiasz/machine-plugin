package com.github.s1maodyasz.machine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class MachineUpgradeConfiguration {

    @NonNull
    @Singular
    private Map<Integer, Double> levels;

}
