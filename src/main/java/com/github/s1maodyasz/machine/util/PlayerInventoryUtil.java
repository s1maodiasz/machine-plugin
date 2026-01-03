package com.github.s1maodyasz.machine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerInventoryUtil {

    public static boolean give(@NotNull Player player, @NotNull ItemStack itemStack) {
        return player.getInventory().addItem(itemStack).isEmpty();
    }
}
