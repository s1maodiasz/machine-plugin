package com.github.s1maodyasz.machine.placement;

import com.github.s1maodyasz.machine.configuration.ConfigurationManager;
import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.message.MessageConstants;
import com.github.s1maodyasz.machine.model.*;
import com.github.s1maodyasz.machine.model.enums.PermissionEnum;
import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;
import com.github.s1maodyasz.machine.provider.CustomEntityProvider;
import com.github.s1maodyasz.machine.util.ItemDataUtil;
import com.github.s1maodyasz.machine.util.MessageBuilder;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Need to refactor, this code is a shit
 */
@RequiredArgsConstructor
public final class MachinePlaceListener implements Listener {

    private static final int RADIUS = 5;

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

        if (clicked == null || item == null || hand == null) {
            MessageBuilder.of(MessageConstants.PLACE_INVALID_ITEM)
                .with(MessageConstants.PLACEHOLDER_PLAYER, player.getName())
                .send(player);
            return;
        }

        final var encoded = ItemDataUtil.of(namespacedKey)
            .get(item, PersistentDataType.STRING)
            .orElse(null);

        if (encoded == null || encoded.isBlank()) {
            MessageBuilder.of(MessageConstants.PLACE_INVALID_ITEM)
                .with(MessageConstants.PLACEHOLDER_PLAYER, player.getName())
                .send(player);
            return;
        }

        final MachineData data = gson.fromJson(encoded, MachineData.class);
        if (data == null) {
            MessageBuilder.of(MessageConstants.PLACE_INVALID_ITEM)
                .with(MessageConstants.PLACEHOLDER_PLAYER, player.getName())
                .send(player);
            return;
        }

        final var configuration = configurationManager.get(data.key());
        if (configuration == null) {
            MessageBuilder.of(MessageConstants.PLACE_INVALID_CONFIG)
                .with(MessageConstants.PLACEHOLDER_PLAYER, player.getName())
                .with(MessageConstants.PLACEHOLDER_KEY, data.key())
                .send(player);
            event.setCancelled(true);
            return;
        }

        final Block target = clicked.getRelative(face);
        if (!target.getType().isAir()) {
            MessageBuilder.of(MessageConstants.PLACE_TARGET_NOT_REPLACEABLE)
                .with(MessageConstants.PLACEHOLDER_PLAYER, player.getName())
                .with(MessageConstants.PLACEHOLDER_KEY, data.key())
                .send(player);
            event.setCancelled(true);
            return;
        }

        final Location placeAt = target.getLocation();
        if (placeAt.getWorld() == null) {
            MessageBuilder.of(MessageConstants.PLACE_WORLD_NULL)
                .with(MessageConstants.PLACEHOLDER_PLAYER, player.getName())
                .with(MessageConstants.PLACEHOLDER_KEY, data.key())
                .send(player);
            event.setCancelled(true);
            return;
        }

        final MachineLocation loc = MachineLocation.builder()
            .worldId(placeAt.getWorld().getUID())
            .x(placeAt.getBlockX())
            .y(placeAt.getBlockY())
            .z(placeAt.getBlockZ())
            .build();

        final List<Machine> nearby;
        try {
            nearby = database.nearbySync(loc, RADIUS);
        } catch (Throwable t) {
            MessageBuilder.of(MessageConstants.ERROR_DATABASE)
                .with(MessageConstants.PLACEHOLDER_PLAYER, player.getName())
                .send(player);
            event.setCancelled(true);
            return;
        }

        if (hasDifferentTypeNearby(nearby, data.key())) {
            MessageBuilder.of(MessageConstants.PLACE_DIFFERENT_TYPE_NEARBY)
                .with(MessageConstants.PLACEHOLDER_PLAYER, player.getName())
                .with(MessageConstants.PLACEHOLDER_KEY, data.key())
                .send(player);
            event.setCancelled(true);
            return;
        }

        if (hasCompatibleNearby(nearby, data.key(), data.levels())
            && !hasStackPermissionNearby(nearby, player.getUniqueId(), data.key(), data.levels())) {
            MessageBuilder.of(MessageConstants.PLACE_STACK_NOT_ALLOWED)
                .with(MessageConstants.PLACEHOLDER_PLAYER, player.getName())
                .with(MessageConstants.PLACEHOLDER_KEY, data.key())
                .send(player);
            event.setCancelled(true);
            return;
        }

        if (!spawnModelDisplay(placeAt, configuration.display())) {
            MessageBuilder.of(MessageConstants.PLACE_SPAWN_DISPLAY_FAILED)
                .with(MessageConstants.PLACEHOLDER_PLAYER, player.getName())
                .with(MessageConstants.PLACEHOLDER_KEY, data.key())
                .send(player);
            event.setCancelled(true);
            return;
        }

        final var machine = Machine.builder()
            .location(loc)
            .key(data.key())
            .stack(data.stack())
            .drops(data.drops())
            .ownerId(player.getUniqueId())
            .upgrades(data.levels())
            .build();

        try {
            database.saveAsync(machine);
        } catch (Throwable t) {
            MessageBuilder.of(MessageConstants.ERROR_DATABASE)
                .with(MessageConstants.PLACEHOLDER_PLAYER, player.getName())
                .send(player);
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
        consumeOne(player, hand);

        MessageBuilder.of(MessageConstants.PLACE_SUCCESS)
            .with(MessageConstants.PLACEHOLDER_PLAYER, player.getName())
            .with(MessageConstants.PLACEHOLDER_KEY, data.key())
            .send(player);
    }

    private static boolean hasDifferentTypeNearby(@NotNull List<Machine> nearby, @NotNull String key) {
        return nearby.stream().anyMatch(m -> !key.equals(m.key()));
    }

    private static boolean hasCompatibleNearby(
        @NotNull List<Machine> nearby,
        @NotNull String key,
        @NotNull Map<UpgradeEnum, Integer> placingLevels
    ) {
        return nearby.stream().anyMatch(m -> key.equals(m.key()) && m.upgrades().equals(placingLevels));
    }

    private static boolean hasStackPermissionNearby(
        @NotNull List<Machine> nearby,
        @NotNull UUID playerId,
        @NotNull String key,
        @NotNull Map<UpgradeEnum, Integer> placingLevels
    ) {
        return nearby.stream()
            .anyMatch(m -> key.equals(m.key())
                && m.upgrades().equals(placingLevels)
                && canAddStack(m, playerId));
    }

    private static boolean canAddStack(@NotNull Machine machine, @NotNull UUID playerId) {
        if (playerId.equals(machine.ownerId())) return true;
        return machine.collaborators().stream()
            .anyMatch(c -> playerId.equals(c.playerId())
                && c.permissions().contains(PermissionEnum.ADD_STACK));
    }

    private boolean spawnModelDisplay(@NotNull Location location, @NotNull MachineDisplayConfiguration display) {
        try {
            if (!display.isModel()) return false;

            final var cfg = (MachineDisplayConfiguration.Model) display;
            final var spawnLoc = location.clone().add(0.5, 0.0, 0.5);

            final var entity = provider.spawn(spawnLoc, cfg.model());
            entity.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "machine");
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
