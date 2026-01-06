package com.github.s1maodyasz.machine.service.provider;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@FunctionalInterface
public interface CustomItemProvider {

    @NotNull
    Optional<ItemStack> resolve(@NotNull String value);
}
