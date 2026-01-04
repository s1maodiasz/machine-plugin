package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.model.BatteryData;
import com.github.s1maodyasz.machine.util.formatter.NumberFormatter;
import org.jetbrains.annotations.NotNull;

public final class BatteryPlaceholderResolver implements PlaceholderResolver<BatteryConfiguration, BatteryData> {

    @Override
    public @NotNull String resolve(String text, BatteryConfiguration configuration, BatteryData data) {
        return text
            .replace("{battery_stack}", NumberFormatter.format(data.stack()))
            .replace("{battery_total}", NumberFormatter.format(configuration.amount() * data.stack()));
    }
}
