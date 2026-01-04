package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.model.BatteryData;
import com.github.s1maodyasz.machine.provider.MiniMessageProvider;
import com.github.s1maodyasz.machine.util.formatter.NumberFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

public final class BatteryPlaceholderResolver implements PlaceholderResolver<BatteryConfiguration, BatteryData> {

    @Override
    public @NotNull Component resolve(@NotNull String text, BatteryConfiguration configuration, BatteryData data) {
        return MiniMessageProvider.MM.deserialize(
                text,
                TagResolver.resolver(Placeholder.parsed("battery_energy", NumberFormatter.format(data.energy()))));
    }
}
