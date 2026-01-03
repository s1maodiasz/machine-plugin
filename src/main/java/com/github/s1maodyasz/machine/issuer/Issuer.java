package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.configuration.ConfigurationManager;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.model.StackableData;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import com.github.s1maodyasz.machine.util.ItemBuilder;
import com.github.s1maodyasz.machine.util.PlayerInventoryUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class Issuer<C, D extends StackableData> {

    protected final Gson gson;
    protected final NamespacedKey namespacedKey;
    protected final CustomItemProvider provider;
    protected final ConfigurationManager<C> configurationManager;

    @Override
    public IssueResult issue(@NotNull Player player, @NotNull String key, MachineData machineData) {
        final var configuration = configurationManager.get(key);
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
        final var nameResolved = MachinePlaceholderResolver.resolve(name, configuration, machineData);

        final var lore = itemConfiguration.lore();
        final var loreResolved = lore.stream()
            .map(line -> MachinePlaceholderResolver.resolve(line, configuration, machineData))
            .toList();

        final var encoded = gson.toJson(machineData);
        final var stack = itemBuilder
            .name(nameResolved)
            .lore(loreResolved)
            .pdc()
            .string(namespacedKey, encoded)
            .build();

        final var added = PlayerInventoryUtil.give(player, stack);
        return added ? IssueResult.SUCCESS : IssueResult.INVENTORY_FULL;
    }
}
