package com.github.s1maodyasz.machine.issuer;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface PlaceholderResolver<C, D> {

    @NotNull
    Component resolve(@NotNull String text, C configuration, D data);

    default @NotNull List<Component> resolveAll(@NotNull List<String> text, C configuration, D data) {
        return text.stream().map(s -> resolve(s, configuration, data)).toList();
    }
}
