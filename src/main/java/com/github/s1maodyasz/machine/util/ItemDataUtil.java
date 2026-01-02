package com.github.s1maodyasz.machine.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemDataUtil {

    private final NamespacedKey key;

    public static ItemDataUtil of(NamespacedKey key) {
        return new ItemDataUtil(key);
    }

    public static ItemDataUtil of(Plugin plugin, String key) {
        final var namespace = new NamespacedKey(plugin, key);
        return new ItemDataUtil(namespace);
    }

    public NamespacedKey raw() {
        return key;
    }

    public <T, Z> Optional<Z> get(ItemStack item, PersistentDataType<T, Z> type) {
        return Optional.ofNullable(getMeta(item))
                .map(ItemMeta::getPersistentDataContainer)
                .map(pdc -> pdc.get(key, type));
    }

    public <T, Z, Q> Optional<Q> get(ItemStack item, PersistentDataType<T, Z> type, Function<Z, Q> adapter) {
        return Optional.ofNullable(getMeta(item))
            .map(ItemMeta::getPersistentDataContainer)
            .map(pdc -> pdc.get(key, type))
            .map(adapter);
    }

    public <T, Z> Z getOr(ItemStack item, PersistentDataType<T, Z> type, Z def) {
        return get(item, type).orElse(def);
    }

    public <T, Z> boolean has(ItemStack item, PersistentDataType<T, Z> type) {
        var meta = getMeta(item);
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(key, type);
    }

    public <T, Z> void set(ItemStack item, PersistentDataType<T, Z> type, Z value) {
        var meta = getMeta(item);
        if (meta == null) return;

        var pdc = meta.getPersistentDataContainer();
        if (value == null) pdc.remove(key);
        else pdc.set(key, type, value);

        item.setItemMeta(meta);
    }

    public void remove(ItemStack item) {
        set(item, PersistentDataType.STRING, null);
    }

    private static ItemMeta getMeta(ItemStack item) {
        if (item == null) return null;
        return item.getItemMeta();
    }
}
