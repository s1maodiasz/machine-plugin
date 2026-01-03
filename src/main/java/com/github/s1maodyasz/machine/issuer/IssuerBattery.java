package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.configuration.ConfigurationManager;
import com.github.s1maodyasz.machine.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.model.BatteryData;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import com.google.gson.Gson;
import org.bukkit.NamespacedKey;

public final class IssuerBattery extends Issuer<BatteryConfiguration, BatteryData> {

    public IssuerBattery(Gson gson, NamespacedKey namespacedKey, CustomItemProvider provider, ConfigurationManager<BatteryConfiguration> configurationManager) {
        super(gson, namespacedKey, provider, configurationManager);
    }
}
