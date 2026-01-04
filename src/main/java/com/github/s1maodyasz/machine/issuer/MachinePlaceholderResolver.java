package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.provider.MiniMessageProvider;
import com.github.s1maodyasz.machine.util.formatter.NumberFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

public final class MachinePlaceholderResolver implements PlaceholderResolver<MachineConfiguration, MachineData> {

    @Override
    public @NotNull Component resolve(@NotNull String text, MachineConfiguration configuration, MachineData data) {
        return MiniMessageProvider.MM.deserialize(
                text, TagResolver.resolver(Placeholder.parsed("machine_drops", NumberFormatter.format(data.drops()))));
    }
}
