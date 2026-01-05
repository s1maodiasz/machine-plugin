package com.github.s1maodyasz.machine;

import co.aikar.commands.PaperCommandManager;
import com.github.s1maodyasz.machine.command.BatteryCommand;
import com.github.s1maodyasz.machine.command.MachineCommand;
import com.github.s1maodyasz.machine.configuration.ConfigurationManager;
import com.github.s1maodyasz.machine.configuration.adapter.BatteryConfigurationSectionAdapter;
import com.github.s1maodyasz.machine.configuration.adapter.MachineConfigurationSectionAdapter;
import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.service.refresher.MachineBatteriesConsumptionProcessors;
import com.github.s1maodyasz.machine.service.refresher.MachineRefresher;
import com.github.s1maodyasz.machine.listener.InventoryClickListener;
import com.github.s1maodyasz.machine.listener.MachineInteractionListener;
import com.github.s1maodyasz.machine.listener.MachinePlaceListener;
import com.github.s1maodyasz.machine.configuration.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.model.BatterySerializeData;
import com.github.s1maodyasz.machine.configuration.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineSerializeData;
import com.github.s1maodyasz.machine.provider.*;
import com.google.gson.GsonBuilder;
import java.util.Objects;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class MachinePlugin extends JavaPlugin {

    @Getter
    private static MachinePlugin instance;

    private MachineDatabase database;
    private ConfigurationManager<MachineConfiguration> machineConfiguration;
    private ConfigurationManager<BatteryConfiguration> batteryConfiguration;
    private MachineRefresher snapshotUpdater;
    private CustomItemProvider customItemProvider;
    private CustomEntityProvider customEntityProvider;
    private AbstractItemIssuer<MachineConfiguration, MachineSerializeData> machineIssuer;
    private AbstractItemIssuer<BatteryConfiguration, BatterySerializeData> batteryIssuer;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        database = new MachineDatabase(instance);
        machineConfiguration = new ConfigurationManager<>();
        final var machinesSection = getConfig().getConfigurationSection("machines");
        Objects.requireNonNull(machinesSection, "Machines section cannot be null");
        for (final var machineSectionKey : machinesSection.getKeys(false)) {
            final var machineSection = machinesSection.getConfigurationSection(machineSectionKey);
            final var machine = MachineConfigurationSectionAdapter.adapt(machineSection);
            machineConfiguration.register(machine.key(), machine);
        }

        batteryConfiguration = new ConfigurationManager<>();
        final var batteriesSection = getConfig().getConfigurationSection("batteries");
        Objects.requireNonNull(batteriesSection, "Batteries section cannot be null");
        for (final var batterySectionKey : batteriesSection.getKeys(false)) {
            final var batterySection = batteriesSection.getConfigurationSection(batterySectionKey);
            final var battery = BatteryConfigurationSectionAdapter.adapt(batterySection);
            batteryConfiguration.register(battery.key(), battery);
        }

        snapshotUpdater = new MachineRefresher(database, new MachineBatteriesConsumptionProcessors());

        var gson = new GsonBuilder().disableHtmlEscaping().create();
        var machineNamespacedKey = new NamespacedKey(instance, "machine_data");
        var batteryNamespacedKey = new NamespacedKey(instance, "battery_data");

        if (Bukkit.getPluginManager().isPluginEnabled("Nexo")) {
            customItemProvider = new NexoProvider();
        } else {
            getLogger().warning("Nexo não encontrado. Compatibilidade desativada (itens custom não serão usados).");
            customItemProvider = new MissingCustomItemProvider();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("BetterModel")) {
            customEntityProvider = new BetterModelProvider();
            getLogger().info("BetterModel encontrado, habilitando compatibilidade.");
        } else {
            getLogger().severe("BetterModel não encontrado. Este plugin requer BetterModel. Desativando plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        final var machinePlaceholderResolver = new MachinePlaceholderResolver();
        machineIssuer = new MachineIssuerAbstract(
                gson, machineNamespacedKey, customItemProvider, machineConfiguration, machinePlaceholderResolver);

        final var batteryPlaceholderResolver = new BatteryPlaceholderResolver();
        batteryIssuer = new BatteryIssuerAbstract(
                gson, batteryNamespacedKey, customItemProvider, batteryConfiguration, batteryPlaceholderResolver);

        var commandManager = new PaperCommandManager(instance);
        commandManager.registerCommand(new MachineCommand(instance, machineIssuer));
        commandManager.registerCommand(new BatteryCommand(instance, batteryIssuer));

        Bukkit.getPluginManager()
                .registerEvents(
                        new MachinePlaceListener(
                                instance,
                                gson,
                                machineNamespacedKey,
                                machineConfiguration,
                                database,
                                customEntityProvider),
                        instance);

        Bukkit.getPluginManager()
                .registerEvents(
                        new MachineInteractionListener(
                                instance, machineNamespacedKey, database, machineConfiguration, snapshotUpdater),
                        instance);

        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), instance);
    }

    @Override
    public void onDisable() {
        database.close();
    }
}
