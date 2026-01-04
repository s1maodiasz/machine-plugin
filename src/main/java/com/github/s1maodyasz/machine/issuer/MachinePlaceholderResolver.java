package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.util.formatter.NumberFormatter;
import org.jetbrains.annotations.NotNull;

public final class MachinePlaceholderResolver implements PlaceholderResolver<MachineConfiguration, MachineData> {

    @Override
    public @NotNull String resolve(String text, MachineConfiguration configuration, MachineData data) {
        return text
            .replace("{machine_stack}", NumberFormatter.format(data.stack()))
            .replace("{machine_drops}", NumberFormatter.format(data.stack()));
    }
}
