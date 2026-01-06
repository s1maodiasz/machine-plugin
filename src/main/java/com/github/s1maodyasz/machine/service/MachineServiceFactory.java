package com.github.s1maodyasz.machine.service;

import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.service.configuration.BatteryConfigurationRegistry;
import com.github.s1maodyasz.machine.service.configuration.ConfigurationRegistries;
import com.github.s1maodyasz.machine.service.configuration.MachineConfigurationRegistry;
import com.github.s1maodyasz.machine.service.item.ItemFactories;
import com.github.s1maodyasz.machine.service.item.ItemFactory;
import com.github.s1maodyasz.machine.service.provider.CustomItemProvider;
import com.github.s1maodyasz.machine.service.provider.MissingCustomItemProvider;
import com.github.s1maodyasz.machine.service.provider.NexoProvider;
import com.github.s1maodyasz.machine.service.state.MachineStateResolver;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MachineServiceFactory {

    public static @NotNull MachineService create(@NotNull Plugin plugin) {
        final var database = new MachineDatabase(plugin);
        final var resolver = new MachineStateResolver(database);
        final var registries = createRegistries(plugin);
        final var factories = createFactories(plugin);
        return new MachineService(database, resolver, registries, factories);
    }

    @ApiStatus.Internal
    private static ConfigurationRegistries createRegistries(@NotNull Plugin plugin) {
        final var machines = new MachineConfigurationRegistry(plugin);
        final var batteries = new BatteryConfigurationRegistry(plugin);
        return new ConfigurationRegistries(machines, batteries);
    }

    @ApiStatus.Internal
    private static ItemFactories createFactories(@NotNull Plugin plugin) {
        final var namespace = plugin.namespace();
        final var key = new NamespacedKey(plugin, namespace);

        final var nexo =
            plugin.getServer().getPluginManager().getPlugin("Nexo");

        CustomItemProvider provider;
        if (nexo != null && nexo.isEnabled())
            provider = new NexoProvider();
        else provider = new MissingCustomItemProvider();

        final var machines = ItemFactory.ofMachine(key, provider);
        final var batteries = ItemFactory.ofBattery(key, provider);
        return new ItemFactories(machines, batteries);
    }
}
