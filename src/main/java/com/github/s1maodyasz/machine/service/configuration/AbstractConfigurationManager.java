package com.github.s1maodyasz.machine.service.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class AbstractConfigurationManager<V> implements ConfigurationManager<String, V> {

	private final Map<String, V> map = new ConcurrentHashMap<>();
	private final Function<V, String> extractor;
	private final Function<String, String> normalizer;

	protected AbstractConfigurationManager(
			Function<V, String> extractor, Function<String, String> normalizer) {
		this.extractor = Objects.requireNonNull(extractor, "Extractor cannot be null.");
		this.normalizer = Objects.requireNonNull(normalizer, "Normalizer cannot be null.");
	}

	@Override
	public void register(V value) {
		Objects.requireNonNull(value, "value");
		register(extractor.apply(value), value);
	}

	@Override
	public void register(String key, V value) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(value, "value");
		map.put(normalizer.apply(key), value);
	}

	@Override
	public void unregister(String key) {
		if (key == null) return;
		map.remove(normalizer.apply(key));
	}

	@Override
	public boolean has(String key) {
		if (key == null) return false;
		return map.containsKey(normalizer.apply(key));
	}

	@Override
	public Optional<V> get(String key) {
		if (key == null) return Optional.empty();
		return Optional.ofNullable(map.get(normalizer.apply(key)));
	}

	@Override
	public V require(String key) {
		return get(key)
				.orElseThrow(() -> new IllegalArgumentException("Unknown configuration: " + key));
	}

	@Override
	public Collection<V> all() {
		return Collections.unmodifiableCollection(map.values());
	}

	@Override
	public void clear() {
		map.clear();
	}
}
