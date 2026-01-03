package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.configuration.ConfigurationManager;
import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import com.github.s1maodyasz.machine.util.ItemBuilder;
import com.github.s1maodyasz.machine.util.PlayerInventoryUtil;
import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class IssuerMachine extends Issuer<MachineConfiguration, MachineData> {

    public IssuerMachine(
        Gson gson,
        NamespacedKey namespacedKey,
        CustomItemProvider provider,
        ConfigurationManager<MachineConfiguration> configurationManager) {
        super(gson, namespacedKey, provider, configurationManager);
    }
}