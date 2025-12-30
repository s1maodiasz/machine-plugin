package com.github.s1maodyasz.machine.provider;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomItemProvider {

    @Nullable ItemStack resolve(@NotNull String id);

    @NotNull String name();

    boolean available();

}