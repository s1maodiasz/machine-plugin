package com.github.s1maodyasz.machine.listener;

import com.github.s1maodyasz.machine.handlers.MachinePlacementResult;
import com.github.s1maodyasz.machine.service.MachineService;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public final class BlockInteractionListener implements Listener {

    private final MachineService service;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final var action = event.getAction();
        if (action.isLeftClick()) return;

        var clicked = event.getClickedBlock();
        if (clicked == null) return;

        var face = event.getBlockFace();
        var target = clicked.getRelative(face);
        if (target.isEmpty()) return;

        final var player = event.getPlayer();
        final var location = target.getLocation();
        final var item = player.getInventory().getItemInMainHand();

        final var result = service.place(player, location, face, item);
        if (result instanceof MachinePlacementResult.Success) {

        }

        event.setCancelled(true);
    }
}
