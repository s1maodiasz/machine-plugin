package com.github.s1maodyasz.machine.configuration.model;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class MachineUpgradeConfiguration {

    @NonNull
    @Singular
    private Map<Integer, Level> levels;

    @Builder(toBuilder = true)
    @Accessors(fluent = true)
    public record Level(double value, double price) { }

}
