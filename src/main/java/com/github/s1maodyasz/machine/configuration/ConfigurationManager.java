package com.github.s1maodyasz.machine.configuration;

import java.util.*;

public final class ConfigurationManager<V> {

    private final Map<String, V> map = new HashMap<>();

    public void register(String key, V value) {
        map.put(key, value);
    }

    public void unregister(String key) {
        map.remove(key);
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
