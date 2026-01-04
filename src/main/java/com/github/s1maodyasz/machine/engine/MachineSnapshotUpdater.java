package com.github.s1maodyasz.machine.engine;

import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * It's like create a screenshot of the state of machine
 * Resolve all values and insert them in db
 */
@RequiredArgsConstructor
public final class MachineSnapshotUpdater {

    private final MachineDatabase database;

    private MachineBatteriesConsumptionProcessor machineBatteriesConsumptionProcessor;

    public @NotNull Machine update(@NotNull Machine machine, final @NotNull MachineConfiguration configuration) {
        var engine = machine.engine();
        if (engine.isIdle()) return machine;

        final long lastInteraction = engine.lastInteractionAt();
        final long now = System.currentTimeMillis();
        final long difference = Math.max(0, now - lastInteraction);

        final double speed = getUpgradeValue(machine, configuration, UpgradeEnum.CYCLE_SPEED); // ciclos/seg
        if (speed <= 0) return machine;

        final long totalCyclesFromLastInteraction = (long) Math.floor(difference * speed / 1000);
        if (totalCyclesFromLastInteraction <= 0) return machine;

        final double energy = machine.energy();
        final double energyCost = getUpgradeValue(machine, configuration, UpgradeEnum.ENERGY_COST);
        if (energyCost <= 0) return machine;

        final long totalPossibleCycles = (long) Math.floor(energy / energyCost);
        final long totalRealCycles = Math.min(totalCyclesFromLastInteraction, totalPossibleCycles);

        if (totalRealCycles <= 0) {
            engine = engine.toBuilder().active(false).build();
            machine = machine.toBuilder().engine(engine).build();
            database.saveAsync(machine);
            return machine;
        }

        final double energyToConsume = totalRealCycles * energyCost;
        machine = MachineBatteriesConsumptionProcessors.process(machine.consumptionMode(), machine, energyToConsume);

        final double outputPerCycle = getUpgradeValue(machine, configuration, UpgradeEnum.OUTPUT_PER_CYCLE);
        final double dropsToAdd = outputPerCycle * totalRealCycles;

        final long cycleTimeMs = Math.max(1, (long) Math.floor(1000 / speed));
        final long newLastInteractionAt = lastInteraction + (totalRealCycles * cycleTimeMs);

        engine = engine.toBuilder().lastInteractionAt(newLastInteractionAt).build();
        machine = machine.toBuilder()
            .drops(machine.drops() + dropsToAdd)
            .engine(engine)
            .build();

        database.saveAsync(machine);
        return machine;
    }

    private double getUpgradeValue(final Machine machine, final MachineConfiguration configuration, final UpgradeEnum upgrade) {
        final var machineOpcUpgradeLevel = machine.upgrades().get(upgrade);
        return configuration.upgrades().get(upgrade).levels().get(machineOpcUpgradeLevel);
    }
}
