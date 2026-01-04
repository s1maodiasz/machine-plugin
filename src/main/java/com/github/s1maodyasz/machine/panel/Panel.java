package com.github.s1maodyasz.machine.panel;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface Panel extends InventoryHolder {

    @Override
    @NotNull
    Inventory getInventory();

    void handleClick(@NotNull InventoryClickEvent event);

    void handleClose(@NotNull InventoryCloseEvent event);

    default boolean isThisInventory(@NotNull InventoryClickEvent event) {
        return event.getInventory().getHolder() == this;
    }

    default void cancel(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
