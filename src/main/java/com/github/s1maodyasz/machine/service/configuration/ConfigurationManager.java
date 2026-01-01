package com.github.s1maodyasz.machine.service.configuration;

import java.util.Collection;
import java.util.Optional;

public interface ConfigurationManager<K, V> {

	void register(V value);

	void register(K key, V value);

	void unregister(K key);

	boolean has(K key);

	Optional<V> get(K key);

	V require(K key);

	Collection<V> all();

	void clear();
}
