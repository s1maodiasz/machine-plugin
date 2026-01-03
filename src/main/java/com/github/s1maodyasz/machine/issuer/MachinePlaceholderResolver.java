package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.util.formatter.NumberFormatter;
import org.jetbrains.annotations.NotNull;

interface MachinePlaceholderResolver extends PlaceholderResolver<MachineConfiguration, MachineData> {

    MachinePlaceholderResolver STACK = (configuration, data) -> NumberFormatter.format(data.stack());
    MachinePlaceholderResolver DROPS = (configuration, data) -> NumberFormatter.format(data.drops());

    static String resolve(
            @NotNull String text, @NotNull MachineConfiguration configuration, @NotNull MachineData data) {
        return text.replace("{machine_stack}", STACK.resolve(configuration, data))
                .replace("{machine_drops}", DROPS.resolve(configuration, data));
    }
}
