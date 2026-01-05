package com.github.s1maodyasz.machine.provider;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class MissingCustomItemProvider implements CustomItemProvider {

    @Override
    public @NotNull Optional<ItemStack> resolve(@NotNull String value) {
        return Optional.empty();
    }
}
