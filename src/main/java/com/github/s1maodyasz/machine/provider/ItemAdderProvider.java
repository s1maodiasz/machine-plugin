package com.github.s1maodyasz.machine.provider;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ItemAdderProvider implements CustomItemProvider {

    private static final String PLUGIN = "ItemsAdder";

    @Override
    public @Nullable ItemStack resolve(@NotNull String id) {
        if (!available()) return null;

        final CustomStack stack = CustomStack.getInstance(id);
        return stack != null ? stack.getItemStack() : null;
    }

    @Override
    public @NotNull String name() {
        return "ItemsAdder";
    }

    @Override
    public boolean available() {
        return Bukkit.getPluginManager().isPluginEnabled(PLUGIN);
    }
}