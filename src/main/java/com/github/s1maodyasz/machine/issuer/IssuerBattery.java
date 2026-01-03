package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.configuration.AbstractConfigurationManager;
import com.github.s1maodyasz.machine.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.model.BatteryData;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import com.github.s1maodyasz.machine.util.ItemBuilder;
import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class IssuerBattery extends Issuer<BatteryConfiguration, BatteryData> {

    public IssuerBattery(
            Gson gson,
            NamespacedKey namespacedKey,
            CustomItemProvider provider,
            AbstractConfigurationManager<BatteryConfiguration> configurationManager) {
        super(gson, namespacedKey, provider, configurationManager);
    }

    @Override
    public IssueResult issue(@NotNull Player player, @NotNull String key, BatteryData machineData) {
        final var configuration = configurationManager.get(key).orElse(null);
        if (configuration == null) return IssueResult.INVALID_KEY;

        final var itemConfiguration = configuration.item();

        final var itemBuilder = ItemBuilder.of();
        try {
            final var model = itemConfiguration.model();
            final var stack = provider.resolve(model);
            itemBuilder.stack(stack);
        } catch (IllegalArgumentException exception) {
            final var material = itemConfiguration.material();
            final var url = itemConfiguration.url();
            itemBuilder.type(material).skullTextureUrl(url);
        }

        final var name = itemConfiguration.name();
        final var nameResolved = BatteryPlaceholderResolver.resolve(name, configuration, machineData);

        final var lore = itemConfiguration.lore();
        final var loreResolved = lore.stream()
                .map(line -> BatteryPlaceholderResolver.resolve(line, configuration, machineData))
                .toList();

        final var encoded = gson.toJson(machineData);
        final var stack = itemBuilder
                .name(nameResolved)
                .lore(loreResolved)
                .pdc()
                .string(namespacedKey, encoded)
                .build();

        return IssueResult.SUCCESS;
    }
}
