package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.model.BatteryData;
import com.github.s1maodyasz.machine.util.formatter.NumberFormatter;
import org.jetbrains.annotations.NotNull;

interface BatteryPlaceholderResolver extends PlaceholderResolver<BatteryConfiguration, BatteryData> {

    BatteryPlaceholderResolver STACK = (configuration, data) -> NumberFormatter.format(data.stack());
    BatteryPlaceholderResolver TOTAL =
            (configuration, data) -> NumberFormatter.format(data.stack() * configuration.amount());

    static String resolve(
            @NotNull String text, @NotNull BatteryConfiguration configuration, @NotNull BatteryData data) {
        return text.replace("{machine_stack}", STACK.resolve(configuration, data))
                .replace("{machine_drops}", TOTAL.resolve(configuration, data));
    }
}
