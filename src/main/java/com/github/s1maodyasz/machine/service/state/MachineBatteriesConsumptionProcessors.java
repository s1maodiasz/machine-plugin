package com.github.s1maodyasz.machine.service.state;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.enums.ConsumptionMode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
final class MachineBatteriesConsumptionProcessors {

    private static final MachineBatteriesConsumptionProcessor ordered = new MachineBatteriesConsumptionProcessor.Ordered();
    private static final MachineBatteriesConsumptionProcessor split = new MachineBatteriesConsumptionProcessor.Split();

    public static @NotNull Machine process(@NotNull Machine machine, double cost) {
        final var consumptionMode = machine.getConsumptionMode();
        return switch (consumptionMode) {
            case ORDERED -> ordered.process(machine, cost);
            case SPLIT -> split.process(machine, cost);
        };
    }

    public static @NotNull MachineBatteriesConsumptionProcessor of(@NotNull ConsumptionMode mode) {
        return switch (mode) {
            case ORDERED -> ordered;
            case SPLIT -> split;
        };
    }
}
