package com.github.s1maodyasz.machine.engine;

import com.github.s1maodyasz.machine.model.BatterySlot;
import com.github.s1maodyasz.machine.model.Machine;
import org.jetbrains.annotations.NotNull;

public interface MachineBatteriesConsumptionProcessor {

    @NotNull Machine process(final @NotNull Machine machine, final double cost);

    final class Ordered implements MachineBatteriesConsumptionProcessor {
        @Override
        public @NotNull Machine process(@NotNull Machine machine, double cost) {
            double remaining = -cost;
            final var batteries = machine.batteries();

            for (int i = 0; i < batteries.size(); i++) {
                var battery = batteries.get(i);
                if (battery.deactivated()) continue;

                remaining += battery.total();

                final double carry = Math.max(0, remaining);
                batteries.set(i, battery.toBuilder().total(carry).build());

                if (remaining >= 0) break;
            }
            return machine;
        }
    }

    final class Split implements MachineBatteriesConsumptionProcessor {

        @Override
        public @NotNull Machine process(@NotNull Machine machine, double cost) {
            if (cost <= 0) return machine;

            final var batteries = machine.batteries();
            final long active = batteries.stream().filter(BatterySlot::activated).count();
            if (active <= 0) return machine;

            final double share = cost / (double) active;
            double carry = 0;

            for (int i = 0; i < batteries.size(); i++) {
                var battery = batteries.get(i);
                if (battery.deactivated()) continue;

                final double current = battery.total();
                final double toPay = share + carry;

                final double paid = Math.min(current, toPay);
                final double newTotal = current - paid;

                batteries.set(i, battery.toBuilder().total(newTotal).build());

                carry = toPay - paid;
            }

            return machine;
        }
    }
}
