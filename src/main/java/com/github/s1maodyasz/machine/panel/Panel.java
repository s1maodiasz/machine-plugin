package com.github.s1maodyasz.machine.panel;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public interface Panel extends InventoryHolder {

    void handleClick(final InventoryClickEvent event);

    void handleClose(final InventoryCloseEvent event);

}
