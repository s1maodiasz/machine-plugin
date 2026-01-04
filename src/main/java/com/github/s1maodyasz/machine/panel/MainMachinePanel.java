package com.github.s1maodyasz.machine.panel;

import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.engine.MachineSnapshotUpdater;
import com.github.s1maodyasz.machine.message.MessageConstants;
import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.enums.UpgradeEnum;
import com.github.s1maodyasz.machine.provider.MiniMessageProvider;
import com.github.s1maodyasz.machine.util.ItemBuilder;
import com.github.s1maodyasz.machine.util.formatter.NumberFormatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class MainMachinePanel extends MachinePanel {

    public MainMachinePanel(Plugin plugin, Machine machine, MachineConfiguration configuration, MachineDatabase database, MachineSnapshotUpdater updater) {
        super(plugin, machine, configuration, database, updater);
        draw();
    }

    private void draw() {
        inventory.clear();

        final double energyCapacity = upgradeValue(UpgradeEnum.ENERGY_CAPACITY);
        final double outputPerCycle = upgradeValue(UpgradeEnum.OUTPUT_PER_CYCLE);
        final double speed = upgradeValue(UpgradeEnum.CYCLE_SPEED);
        final double energyCost = upgradeValue(UpgradeEnum.ENERGY_COST);
        final double batterySlots = upgradeValue(UpgradeEnum.BATTERY_SLOTS);

        final long cycleTimeMs = speed > 0D ? (long) Math.floor(1000D / speed) : 0L;

        final var info = ItemBuilder.of(Material.NETHER_STAR)
                .name("&eInformações da Máquina")
                .lore(
                        "",
                        " &7Energia atual: &a" + NumberFormatter.format(machine.energy()) + "/&7"
                                + NumberFormatter.format(energyCapacity),
                        "",
                        " &7Drops por ciclo: &a" + NumberFormatter.format(outputPerCycle),
                        " &7Tempo por ciclo: &a" + NumberFormatter.format(cycleTimeMs) + "ms",
                        " &7Consumo por rodada: &a" + NumberFormatter.format(energyCost),
                        " &7Slots de baterias: &a" + NumberFormatter.format(batterySlots),
                        "")
                .build();

        final var upgrades = ItemBuilder.of(Material.GOLD_INGOT)
                .name("&eAtualizações")
                .lore("&7Clique para abrir o painel de", "&7aprimoramentos da maquina", "", "&eClique para abrir")
                .build();

        final var collaborators = ItemBuilder.of(Material.BOOK)
                .name("&eColaboradores")
                .lore(
                        " &7Clique para gerenciar os colaboradores",
                        "&7com acesso a esta maquina",
                        "",
                        "&eClique para abrir")
                .build();

        final var batteries = ItemBuilder.of(Material.REPEATER)
                .name("&eBaterias")
                .lore(
                        " &7Clique aqui para gerenciar as baterias",
                        "&7que fazem sua maquina funcionar",
                        "",
                        "&eClique para abrir")
                .build();

        final ItemStack dropsItem;
        final double drops = machine.getDrops();
        if (drops <= 0D) {
            dropsItem = ItemBuilder.of(Material.MINECART)
                    .name("&cSem drops para recolher")
                    .build();
        } else {
            dropsItem = ItemBuilder.of(Material.CHEST_MINECART)
                    .name("&eClique para recolher &f" + NumberFormatter.format(drops) + " &edrops")
                    .build();
        }

        inventory.setItem(10, info);
        inventory.setItem(13, upgrades);
        inventory.setItem(14, collaborators);
        inventory.setItem(15, batteries);
        inventory.setItem(16, dropsItem);
    }

    @Override
    public void handleClick(@NotNull InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!isThisInventory(event)) return;

        cancel(event);
        if (event.getClickedInventory() == null
                || event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        switch (event.getSlot()) {
            case 10, 13, 14, 15 -> player.sendMessage("§cFuncionalidade não implementada.");

            case 16 -> {
                updater.update(machine, configuration);

                final double drops = machine.getDrops();
                if (drops == 0D) {
                    player.sendMessage(
                        MiniMessageProvider.MM.deserialize(
                            MessageConstants.COLLECT_EMPTY));
                    return;
                }

                final var newEngine = machine.getEngine();
                newEngine.setLastInteractionNow();

                machine.setDrops(0);
                machine.setEngine(newEngine);

                var value = drops * configuration.drop();

                database.saveAsync(machine).thenRun(() -> {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.sendMessage(MiniMessageProvider.MM.deserialize(
                            MessageConstants.COLLECT_EMPTY,
                            TagResolver.builder()
                                .resolver(Placeholder.parsed("drops", NumberFormatter.format(drops)))
                                .resolver(Placeholder.parsed("value", NumberFormatter.format(value)))
                                .build()
                        ));
                    });
                });

                player.closeInventory();
            }

            default -> {}
        }
    }

    @Override
    public void handleClose(@NotNull InventoryCloseEvent event) {}

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private double upgradeValue(@NotNull UpgradeEnum upgrade) {
        final int level = machine.getUpgrades().getOrDefault(upgrade.name(), 0);
        final var cfg = configuration.upgrades().get(upgrade);
        if (cfg == null || cfg.levels().isEmpty()) return 0D;

        final var levels = cfg.levels();
        if (levels instanceof java.util.NavigableMap<Integer, Double> nav) {
            final var entry = nav.floorEntry(level);
            return entry == null ? 0D : entry.getValue();
        }

        return levels.getOrDefault(level, 0D);
    }
}
