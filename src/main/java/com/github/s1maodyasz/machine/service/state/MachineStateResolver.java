package com.github.s1maodyasz.machine.service.state;

import com.github.s1maodyasz.machine.service.configuration.model.MachineConfiguration;
import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MachineStateResolver {

    private static final long MILLIS_PER_SECOND = 1000L;

    private static final int DEFAULT_UPGRADE_LEVEL = 0;

    private static final long NO_CYCLES = 0L;
    private static final double NO_VALUE = 0D;

    private static final long MIN_CYCLE_TIME_MS = 1L;

    private final MachineDatabase database;

    public @NotNull Machine update(@NotNull Machine machine, @NotNull MachineConfiguration configuration) {
        final var engine = machine.getEngine();
        if (engine.isIdle()) return machine;

        final long li = engine.getLastInteractionAt();
        final long now = System.currentTimeMillis();
        final long elapsed = Math.max(NO_CYCLES, now - li);

        final int sl = machine.getUpgrades().getOrDefault(UpgradeEnum.CYCLE_SPEED.toString(), DEFAULT_UPGRADE_LEVEL);
        final double s = configuration.valueAt(UpgradeEnum.CYCLE_SPEED, sl);
        if (s <= NO_VALUE) return machine;

        final long cfe = (long) ((elapsed * s) / MILLIS_PER_SECOND);
        if (cfe <= NO_CYCLES) return machine;

        final double energy = machine.energy();

        final int ecl = machine.getUpgrades().getOrDefault(UpgradeEnum.ENERGY_COST.toString(), DEFAULT_UPGRADE_LEVEL);
        final double ec = configuration.valueAt(UpgradeEnum.ENERGY_COST, ecl);
        if (ec <= NO_VALUE)
            return machine;

        final long possibleCycles = (long) (energy / ec);
        final long rc = Math.min(cfe, possibleCycles);

        if (rc <= NO_CYCLES)
            return outOfEnergy(machine, engine);

        final double etc = rc * ec;
        machine = MachineBatteriesConsumptionProcessors.process(machine, etc);

        final int ol = machine.getUpgrades().getOrDefault(UpgradeEnum.OUTPUT_PER_CYCLE.toString(), DEFAULT_UPGRADE_LEVEL);
        final double opc = configuration.valueAt(UpgradeEnum.OUTPUT_PER_CYCLE, ol);

        final double dta = opc * rc;

        final long ct = Math.max(MIN_CYCLE_TIME_MS, (long) (MILLIS_PER_SECOND / s));
        final long newLastInteractionAt = li + (rc * ct);

        final var drops = machine.getDrops();
        machine.setDrops(drops + dta);
        machine.getEngine().setLastInteractionAt(newLastInteractionAt);
        database.saveAsync(machine);

        return machine;
    }

    private @NotNull Machine outOfEnergy(@NotNull Machine machine, @NotNull com.github.s1maodyasz.machine.model.MachineEngine engine) {
        engine.setActive(false);
        machine.setEngine(engine);
        database.saveAsync(machine);
        return machine;
    }
}
