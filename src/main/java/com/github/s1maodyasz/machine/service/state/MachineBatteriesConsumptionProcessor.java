package com.github.s1maodyasz.machine.service.state;

import com.github.s1maodyasz.machine.model.BatterySlot;
import com.github.s1maodyasz.machine.model.Machine;
import org.jetbrains.annotations.NotNull;

interface MachineBatteriesConsumptionProcessor {

    @NotNull
    Machine process(final @NotNull Machine machine, final double cost);

    final class Ordered implements MachineBatteriesConsumptionProcessor {
        @Override
        public @NotNull Machine process(@NotNull Machine machine, double cost) {
            double remaining = -cost;
            final var batteries = machine.getBatteries();

            for (int i = 0; i < batteries.size(); i++) {
                var battery = batteries.get(i);
                if (battery.deactivated()) continue;

                remaining += battery.getTotal();

                final double carry = Math.max(0, remaining);
                battery.setTotal(carry);
                batteries.set(i, battery);

                if (remaining >= 0) break;
            }
            return machine;
        }
    }

    final class Split implements MachineBatteriesConsumptionProcessor {

        @Override
        public @NotNull Machine process(@NotNull Machine machine, double cost) {
            if (cost <= 0) return machine;

            final var batteries = machine.getBatteries();
            final long active =
                    batteries.stream().filter(BatterySlot::isActivated).count();
            if (active <= 0) return machine;

            final double share = cost / (double) active;
            double carry = 0;

            for (int i = 0; i < batteries.size(); i++) {
                var battery = batteries.get(i);
                if (battery.deactivated()) continue;

                final double current = battery.getTotal();
                final double pay = share + carry;

                final double paid = Math.min(current, pay);
                final double newTotal = current - paid;

                battery.setTotal(newTotal);
                batteries.set(i, battery);

                carry = pay - paid;
            }

            return machine;
        }
    }
}
