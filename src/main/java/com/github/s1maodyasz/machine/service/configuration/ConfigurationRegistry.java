package com.github.s1maodyasz.machine.service.configuration;

import com.github.s1maodyasz.machine.service.configuration.loader.ConfigurationLoader;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public abstract class ConfigurationRegistry<V> {

    private final Map<String, V> map = new HashMap<>();

    public ConfigurationRegistry(@NotNull Function<V, String> extractor, @NotNull ConfigurationLoader<V> loader, @NotNull Plugin plugin) {
        loader.load(plugin).forEach(v -> register(extractor.apply(v), v));
    }

    public void register(String key, V value) {
        map.put(key, value);
    }

    public V get(String key) {
        return map.get(key);
    }

    public Collection<V> all() {
        final var values = map.values();
        return Collections.unmodifiableCollection(values);
    }

    public void clear() {
        map.clear();
    }
}
