package com.github.s1maodyasz.machine.service.refresher;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.enums.ConsumptionMode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
public final class MachineBatteriesConsumptionProcessors {

    private final MachineBatteriesConsumptionProcessor ordered = new MachineBatteriesConsumptionProcessor.Ordered();
    private final MachineBatteriesConsumptionProcessor split = new MachineBatteriesConsumptionProcessor.Split();

    public @NotNull Machine process(@NotNull ConsumptionMode mode, @NotNull Machine machine, double cost) {
        return switch (mode) {
            case ORDERED -> ordered.process(machine, cost);
            case SPLIT -> split.process(machine, cost);
        };
    }

    public @NotNull MachineBatteriesConsumptionProcessor of(@NotNull ConsumptionMode mode) {
        return switch (mode) {
            case ORDERED -> ordered;
            case SPLIT -> split;
        };
    }
}
