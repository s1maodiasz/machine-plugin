package com.github.s1maodyasz.machine.service.item;

import com.github.s1maodyasz.machine.configuration.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.configuration.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.*;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import com.github.s1maodyasz.machine.util.ItemBuilder;
import com.github.s1maodyasz.machine.util.MaterialUtil;
import com.github.s1maodyasz.machine.util.formatter.NumberFormatter;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public abstract class ItemFactory<C extends ItemConfigurable, D extends SerializeData> {

    protected final MiniMessage message = MiniMessage.miniMessage();

    protected final @NotNull Gson gson;
    protected final @NotNull NamespacedKey key;
    protected final @NotNull CustomItemProvider itemProvider;

    public @NotNull ItemStack create(@NotNull Player player, @NonNull C configuration, @NonNull D data) {
        final var item = configuration.item();

        final var model = item.model();
        final var stack = itemProvider.resolve(model)
            .orElseGet(() -> {
                final var materialName = item.material();
                final var material = MaterialUtil.valueOf(materialName);
                final var url = item.url();
                return ItemBuilder.of(material).skullTextureUrl(url).build();
            });

        final var rawName = item.name();
        final var name = resolveDetail(rawName, configuration, data);

        final var lore = item.lore().stream().map(l -> resolveDetail(l, configuration, data)).toList();

        final var encoded = gson.toJson(data);
        return ItemBuilder.of(stack).name(name).lore(lore).pdc().set(key, PersistentDataType.STRING, encoded).build();
    }

    public @NotNull ItemBuilder builderOf(@NotNull Player player, @NonNull C configuration, @NonNull D data) {
        final var item = create(player, configuration, data);
        return ItemBuilder.of(item);
    }

    /**
     * Basically this is the method to resolve the placeholders on a string
     */
    abstract Component resolveDetail(@NotNull String text, @NotNull C configuration, @NotNull D data);

    public static final class Machine extends ItemFactory<MachineConfiguration, MachineSerializeData> {
        public Machine(@NotNull Gson gson, @NotNull NamespacedKey key, @NotNull CustomItemProvider itemProvider) {
            super(gson, key, itemProvider);
        }

        @Override
        Component resolveDetail(@NotNull String text, @NonNull MachineConfiguration configuration, @NonNull MachineSerializeData data) {
            final var drops = data.drops();
            return message.deserialize(
                text,
                TagResolver.resolver(
                    Placeholder.parsed("drops", NumberFormatter.format(drops))
                )
            );
        }
    }

    public static final class Battery extends ItemFactory<BatteryConfiguration, BatterySerializeData> {
        public Battery(@NotNull Gson gson, @NotNull NamespacedKey key, @NotNull CustomItemProvider itemProvider) {
            super(gson, key, itemProvider);
        }

        @Override
        Component resolveDetail(@NotNull String text, @NonNull BatteryConfiguration configuration, @NonNull BatterySerializeData data) {
            final var energy = data.energy();
            return message.deserialize(
                text,
                TagResolver.resolver(
                    Placeholder.parsed("energy", NumberFormatter.format(energy))
                )
            );
        }
    }
}
