package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.configuration.ConfigurationManager;
import com.github.s1maodyasz.machine.model.Data;
import com.github.s1maodyasz.machine.model.ItemConfigurable;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import com.github.s1maodyasz.machine.util.ItemBuilder;
import com.github.s1maodyasz.machine.util.PlayerInventoryUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class ItemIssuer<C extends ItemConfigurable, D extends Data> {

    protected final Gson gson;
    protected final NamespacedKey namespacedKey;
    protected final CustomItemProvider provider;
    protected final ConfigurationManager<C> configurationManager;
    protected final PlaceholderResolver<C, D> placeholderResolver;

    public IssueResult issue(@NotNull Player player, D data, int amount) {
        final var key = data.key();
        final C configuration = configurationManager.get(key);
        if (configuration == null) return IssueResult.INVALID_KEY;

        final var itemConfiguration = configuration.item();

        final var itemBuilder = ItemBuilder.of(Material.STONE);
        try {
            final var model = itemConfiguration.model();
            final var stack = provider.resolve(model);
            itemBuilder.stack(stack);
        } catch (IllegalStateException exception) {
            final var material = itemConfiguration.material();
            final var url = itemConfiguration.url();
            itemBuilder.type(material).skullTextureUrl(url);
        }

        final var name = itemConfiguration.name();
        final var nameComponent = placeholderResolver.resolve(name, configuration, data);

        final var lore = itemConfiguration.lore();
        final var loreComponent = placeholderResolver.resolveAll(lore, configuration, data);

        final var encoded = gson.toJson(data);
        final var stack = itemBuilder
                .name(nameComponent)
                .lore(loreComponent)
                .amount(amount)
                .pdc()
                .string(namespacedKey, encoded)
                .build();

        final var added = PlayerInventoryUtil.give(player, stack);
        return added ? IssueResult.SUCCESS : IssueResult.INVENTORY_FULL;
    }
}
