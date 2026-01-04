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
    private final MachineBatteriesConsumptionProcessors processors;

    public @NotNull Machine update(@NotNull Machine machine, final @NotNull MachineConfiguration configuration) {
        var engine = machine.getEngine();
        if (engine.isIdle()) return machine;

        final var lastInteraction = engine.getLastInteractionAt();
        final var now = System.currentTimeMillis();
        final var difference = Math.max(0, now - lastInteraction);

        final var speed = getUpgradeValue(machine, configuration, UpgradeEnum.CYCLE_SPEED);
        if (speed <= 0) return machine;

        final var totalCyclesFromLastInteraction = (long) Math.floor(difference * speed / 1000);
        if (totalCyclesFromLastInteraction <= 0) return machine;

        final var energy = machine.energy();
        final var energyCost = getUpgradeValue(machine, configuration, UpgradeEnum.ENERGY_COST);
        if (energyCost <= 0) return machine;

        final var totalPossibleCycles = (long) Math.floor(energy / energyCost);
        final var totalRealCycles = Math.min(totalCyclesFromLastInteraction, totalPossibleCycles);

        if (totalRealCycles <= 0) {
            engine.setActive(false);
            machine.setEngine(engine);
            database.saveAsync(machine);
            return machine;
        }

        final var energyToConsume = totalRealCycles * energyCost;
        machine = processors.process(machine.getConsumptionMode(), machine, energyToConsume);

        final var outputPerCycle = getUpgradeValue(machine, configuration, UpgradeEnum.OUTPUT_PER_CYCLE);
        final var dropsToAdd = outputPerCycle * totalRealCycles;

        final var cycleTimeMs = Math.max(1, (long) Math.floor(1000 / speed));
        final var newLastInteractionAt = lastInteraction + (totalRealCycles * cycleTimeMs);

        var drops = machine.getDrops();
        drops += dropsToAdd;

        engine.setLastInteractionAt(newLastInteractionAt);
        machine.setDrops(drops);
        machine.setEngine(engine);

        database.saveAsync(machine);
        return machine;
    }

    private double getUpgradeValue(
            final Machine machine, final MachineConfiguration configuration, final UpgradeEnum upgrade) {
        final var machineOpcUpgradeLevel = machine.getUpgrades().get(upgrade.toString());
        return configuration.upgrades().get(upgrade).levels().get(machineOpcUpgradeLevel);
    }
}
