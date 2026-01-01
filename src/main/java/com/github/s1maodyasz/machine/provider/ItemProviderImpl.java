package com.github.s1maodyasz.machine.provider;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ItemProviderImpl implements ItemProvider {

    @Override
    public @NotNull ItemStack resolve(@NotNull String value) {
        final Material material = Material.matchMaterial(value);
        if (material == null) throw new IllegalArgumentException("Invalid material named " + value);
        return new ItemStack(material);
    }
}
