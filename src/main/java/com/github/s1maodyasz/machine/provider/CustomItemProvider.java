package com.github.s1maodyasz.machine.provider;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CustomItemProvider {

    @NotNull
    ItemStack resolve(@NotNull String value);
}
