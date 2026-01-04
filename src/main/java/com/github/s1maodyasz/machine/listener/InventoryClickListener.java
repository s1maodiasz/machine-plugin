package com.github.s1maodyasz.machine.listener;

import com.github.s1maodyasz.machine.panel.Panel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public final class InventoryClickListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(final InventoryClickEvent event) {
        final var inventory = event.getInventory();

        final var holder = inventory.getHolder();
        if (holder instanceof Panel panel) panel.handleClick(event);
    }
}
