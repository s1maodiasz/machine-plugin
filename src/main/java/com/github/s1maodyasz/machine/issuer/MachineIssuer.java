package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.configuration.ConfigurationManager;
import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import com.google.gson.Gson;
import org.bukkit.NamespacedKey;

public final class MachineIssuer extends Issuer<MachineConfiguration, MachineData> {

    public MachineIssuer(Gson gson, NamespacedKey namespacedKey, CustomItemProvider provider, ConfigurationManager<MachineConfiguration> configurationManager, PlaceholderResolver<MachineConfiguration, MachineData> placeholderResolver) {
        super(gson, namespacedKey, provider, configurationManager, placeholderResolver);
    }
}