package com.github.s1maodyasz.machine.issuer;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PlaceholderResolver<C, D> {

    @NotNull String resolve(final String text, final C configuration, final D data);

    default @NotNull List<String> resolveAll(final List<String> text, final C configuration, final D data) {
        return text
            .stream()
            .map(s -> resolve(s, configuration, data))
            .toList();
    }
}
