package com.github.s1maodyasz.machine.listener;

import com.github.s1maodyasz.machine.configuration.ConfigurationManager;
import com.github.s1maodyasz.machine.configuration.model.MachineConfiguration;
import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.message.MessageConstants;
import com.github.s1maodyasz.machine.model.*;
import com.github.s1maodyasz.machine.provider.CustomEntityProvider;
import com.github.s1maodyasz.machine.util.ItemDataUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MachinePlaceListener implements Listener {

    private static final int RADIUS = 5;

    private final Plugin plugin;
    private final Gson gson;
    private final NamespacedKey namespacedKey;
    private final ConfigurationManager<MachineConfiguration> configurationManager;
    private final MachineDatabase database;
    private final CustomEntityProvider provider;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(@NotNull PlayerInteractEvent event) {
        final var action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK) return;

        final var player = event.getPlayer();
        final var clicked = event.getClickedBlock();
        final var item = event.getItem();
        final var face = event.getBlockFace();
        final var hand = event.getHand();

        if (clicked == null || item == null || hand == null) return;

        final var encoded = ItemDataUtil.of(namespacedKey)
                .get(item, PersistentDataType.STRING)
                .orElse(null);

        if (encoded == null || encoded.isBlank()) return;

        final MachineSerializeData data = gson.fromJson(encoded, MachineSerializeData.class);
        if (data == null) return;

        final var configuration = configurationManager.get(data.key());
        if (configuration == null) {
            event.setCancelled(true);
            player.sendMessage(MiniMessageProvider.MM.deserialize(
                    MessageConstants.PLACE_INVALID_CONFIG,
                    TagResolver.builder()
                            .resolver(Placeholder.parsed("player", player.getName()))
                            .resolver(Placeholder.parsed("key", data.key()))
                            .build()));
            return;
        }

        final var target = clicked.getRelative(face);
        if (!target.getType().isAir()) {
            event.setCancelled(true);
            player.sendMessage(MiniMessageProvider.MM.deserialize(
                    MessageConstants.PLACE_TARGET_NOT_REPLACEABLE,
                    TagResolver.builder()
                            .resolver(Placeholder.parsed("player", player.getName()))
                            .resolver(Placeholder.parsed("key", data.key()))
                            .build()));
            return;
        }

        final var placeAt = target.getLocation();
        if (placeAt.getWorld() == null) {
            event.setCancelled(true);
            player.sendMessage(MiniMessageProvider.MM.deserialize(
                    MessageConstants.PLACE_WORLD_NULL,
                    TagResolver.builder()
                            .resolver(Placeholder.parsed("player", player.getName()))
                            .resolver(Placeholder.parsed("key", data.key()))
                            .build()));
            return;
        }

        final var loc = new MachineLocation(placeAt.getWorld().getUID(), placeAt.getBlockX(), placeAt.getBlockY(), placeAt.getBlockZ());

        final List<Machine> nearby;
        try {
            nearby = database.nearbySync(loc, RADIUS);
        } catch (Throwable t) {
            event.setCancelled(true);
            player.sendMessage(MiniMessageProvider.MM.deserialize(
                    MessageConstants.ERROR_DATABASE,
                    TagResolver.builder()
                            .resolver(Placeholder.parsed("player", player.getName()))
                            .build()));
            return;
        }

        if (hasDifferentTypeNearby(nearby, data.key())) {
            event.setCancelled(true);
            player.sendMessage(MiniMessageProvider.MM.deserialize(
                    MessageConstants.PLACE_DIFFERENT_TYPE_NEARBY,
                    TagResolver.builder()
                            .resolver(Placeholder.parsed("player", player.getName()))
                            .resolver(Placeholder.parsed("key", data.key()))
                            .build()));
            return;
        }

        if (!spawnModelDisplay(placeAt, configuration.model())) {
            event.setCancelled(true);
            player.sendMessage(MiniMessageProvider.MM.deserialize(
                    MessageConstants.PLACE_SPAWN_DISPLAY_FAILED,
                    TagResolver.builder()
                            .resolver(Placeholder.parsed("player", player.getName()))
                            .resolver(Placeholder.parsed("key", data.key()))
                            .build()));
            return;
        }

        final var machine = new Machine();
        machine.setLocation(loc);
        machine.setKey(data.key());
        machine.setDrops(data.drops());
        machine.setOwnerId(player.getUniqueId());

        var upgrades = new HashMap<String, Integer>();
        for (var entry : data.levels().entrySet())
            upgrades.put(entry.getKey().toString(), entry.getValue());
        machine.setUpgrades(upgrades);

        try {
            database.saveSync(machine);
        } catch (Throwable t) {
            t.printStackTrace();
            event.setCancelled(true);
            player.sendMessage(MiniMessageProvider.MM.deserialize(
                    MessageConstants.ERROR_DATABASE,
                    TagResolver.builder()
                            .resolver(Placeholder.parsed("player", player.getName()))
                            .build()));
            return;
        }

        event.setCancelled(true);
        consumeOne(player, hand);

        player.sendMessage(MiniMessageProvider.MM.deserialize(
                MessageConstants.PLACE_SUCCESS,
                TagResolver.builder()
                        .resolver(Placeholder.parsed("player", player.getName()))
                        .resolver(Placeholder.parsed("key", data.key()))
                        .build()));
    }

    private static boolean hasDifferentTypeNearby(@NotNull List<Machine> nearby, @NotNull String key) {
        return nearby.stream().anyMatch(m -> !key.equals(m.getKey()));
    }

    private boolean spawnModelDisplay(@NotNull Location location, @NotNull String model) {
        try {
            final var spawnLoc = location.clone().add(0.5, 0.0, 0.5);
            final var entity = provider.spawn(spawnLoc, model);
            entity.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private static void consumeOne(@NotNull Player player, @NotNull EquipmentSlot hand) {
        if (player.getGameMode() == GameMode.CREATIVE) return;

        final var inv = player.getInventory();
        final var current = (hand == EquipmentSlot.OFF_HAND) ? inv.getItemInOffHand() : inv.getItemInMainHand();

        final int amount = current.getAmount();
        if (amount <= 1) {
            if (hand == EquipmentSlot.OFF_HAND) inv.setItemInOffHand(null);
            else inv.setItemInMainHand(null);
            return;
        }

        current.setAmount(amount - 1);
        if (hand == EquipmentSlot.OFF_HAND) inv.setItemInOffHand(current);
        else inv.setItemInMainHand(current);
    }
}
