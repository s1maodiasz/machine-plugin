package com.github.s1maodyasz.machine.engine;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.enums.ConsumptionMode;
import org.jetbrains.annotations.NotNull;

public final class MachineBatteriesConsumptionProcessors {

    private static final MachineBatteriesConsumptionProcessor ORDERED =
            new MachineBatteriesConsumptionProcessor.Ordered();

    private static final MachineBatteriesConsumptionProcessor SPLIT =
            new MachineBatteriesConsumptionProcessor.Split();

    private MachineBatteriesConsumptionProcessors() {}

    public static @NotNull Machine process(
            @NotNull ConsumptionMode mode,
            @NotNull Machine machine,
            double cost
    ) {
        return switch (mode) {
            case ORDERED -> ORDERED.process(machine, cost);
            case SPLIT -> SPLIT.process(machine, cost);
        };
    }

    public static @NotNull MachineBatteriesConsumptionProcessor of(@NotNull ConsumptionMode mode) {
        return switch (mode) {
            case ORDERED -> ORDERED;
            case SPLIT -> SPLIT;
        };
    }
}
