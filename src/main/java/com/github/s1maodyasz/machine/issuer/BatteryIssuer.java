package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.configuration.ConfigurationManager;
import com.github.s1maodyasz.machine.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.model.BatteryData;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import com.google.gson.Gson;
import org.bukkit.NamespacedKey;

public final class BatteryIssuer extends ItemIssuer<BatteryConfiguration, BatteryData> {

    public BatteryIssuer(
            Gson gson,
            NamespacedKey namespacedKey,
            CustomItemProvider provider,
            ConfigurationManager<BatteryConfiguration> configurationManager,
            PlaceholderResolver<BatteryConfiguration, BatteryData> placeholderResolver) {
        super(gson, namespacedKey, provider, configurationManager, placeholderResolver);
    }
}
