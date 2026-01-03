package com.github.s1maodyasz.machine.placement;

import com.github.s1maodyasz.machine.configuration.AbstractConfigurationManager;
import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.model.*;
import com.github.s1maodyasz.machine.model.enums.MachinePermissionEnum;
import com.github.s1maodyasz.machine.model.enums.MachineUpgradeEnum;
import com.github.s1maodyasz.machine.provider.CustomEntityProvider;
import com.github.s1maodyasz.machine.util.ItemDataUtil;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MachinePlaceListener implements Listener {

    private static final int RADIUS = 5;

    private final Gson gson;
    private final NamespacedKey namespacedKey;
    private final AbstractConfigurationManager<MachineConfiguration> configurationManager;
    private final MachineDatabase database;
    private final CustomEntityProvider provider;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(@NotNull PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        final Player player = event.getPlayer();
        final Block clicked = event.getClickedBlock();
        final ItemStack item = event.getItem();
        final BlockFace face = event.getBlockFace();
        final EquipmentSlot hand = event.getHand();

        if (clicked == null || item == null || hand == null) return;

        final MachineData data = readData(item);
        if (data == null) return;

        final var config = configurationManager.get(data.key()).orElse(null);
        if (config == null) return;

        final Block target = clicked.getRelative(face);
        if (!isReplaceable(target)) {
            event.setCancelled(true);
            return;
        }

        final Location placeAt = target.getLocation();
        if (placeAt.getWorld() == null) return;

        final MachineLocation loc = MachineLocation.builder()
                .worldId(placeAt.getWorld().getUID())
                .x(placeAt.getBlockX())
                .y(placeAt.getBlockY())
                .z(placeAt.getBlockZ())
                .build();

        final List<Machine> nearby = database.nearbySync(loc, RADIUS);

        if (hasDifferentTypeNearby(nearby, data.key())) {
            event.setCancelled(true);
            return;
        }

        if (hasCompatibleNearby(nearby, data.key(), data.levels())
                && !hasStackPermissionNearby(nearby, player.getUniqueId(), data.key(), data.levels())) {
            event.setCancelled(true);
            return;
        }

        if (!spawnDisplay(placeAt, face, config.display())) {
            event.setCancelled(true);
            return;
        }

        final Machine machine = Machine.builder()
                .location(loc)
                .key(data.key())
                .stack(data.stack())
                .drops(data.drops())
                .ownerId(player.getUniqueId())
                .upgrades(data.levels())
                .build();

        database.saveAsync(machine);

        event.setCancelled(true);
        consumeOne(player, hand);
    }

    private MachineData readData(@NotNull ItemStack stack) {
        final String encoded = ItemDataUtil.of(namespacedKey)
                .get(stack, PersistentDataType.STRING)
                .orElse(null);
        if (encoded == null) return null;
        return gson.fromJson(encoded, MachineData.class);
    }

    private static boolean isReplaceable(@NotNull Block block) {
        final Material type = block.getType();
        return type.isAir() || type == Material.CAVE_AIR || type == Material.VOID_AIR;
    }

    private static boolean hasDifferentTypeNearby(@NotNull List<Machine> nearby, @NotNull String key) {
        return nearby.stream().anyMatch(m -> !key.equals(m.key()));
    }

    private static boolean hasCompatibleNearby(
            @NotNull List<Machine> nearby,
            @NotNull String key,
            @NotNull Map<MachineUpgradeEnum, Integer> placingLevels) {
        return nearby.stream().anyMatch(m -> key.equals(m.key()) && m.upgrades().equals(placingLevels));
    }

    private static boolean hasStackPermissionNearby(
            @NotNull List<Machine> nearby,
            @NotNull UUID playerId,
            @NotNull String key,
            @NotNull Map<MachineUpgradeEnum, Integer> placingLevels) {
        return nearby.stream()
                .anyMatch(m -> key.equals(m.key()) && m.upgrades().equals(placingLevels) && canAddStack(m, playerId));
    }

    private static boolean canAddStack(@NotNull Machine machine, @NotNull UUID playerId) {
        if (playerId.equals(machine.ownerId())) return true;

        return machine.collaborators().stream()
                .anyMatch(c ->
                        playerId.equals(c.playerId()) && c.permissions().contains(MachinePermissionEnum.ADD_STACK));
    }

    private boolean spawnDisplay(
            @NotNull Location location, @NotNull BlockFace face, @NotNull MachineDisplayConfiguration display) {
        try {
            if (display.isBlock()) {
                placeBlockDisplay(location, face, (MachineDisplayConfiguration.Block) display);
                return true;
            }
            if (display.isModel()) {
                spawnModelDisplay(location, (MachineDisplayConfiguration.Model) display);
                return true;
            }
            return false;
        } catch (Throwable t) {
            return false;
        }
    }

    private void placeBlockDisplay(
            @NotNull Location location, @NotNull BlockFace face, @NotNull MachineDisplayConfiguration.Block cfg) {
        final Block block = location.getBlock();
        block.setType(cfg.material(), false);

        final var data = block.getBlockData();
        if (data instanceof Directional directional) {
            directional.setFacing(face);
            block.setBlockData(directional);
        }
    }

    private void spawnModelDisplay(@NotNull Location location, @NotNull MachineDisplayConfiguration.Model cfg) {
        final var spawnLoc = location.clone().add(0.5, 0.0, 0.5);
        final var entity = provider.spawn(spawnLoc, cfg.model());
        entity.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "machine");
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
