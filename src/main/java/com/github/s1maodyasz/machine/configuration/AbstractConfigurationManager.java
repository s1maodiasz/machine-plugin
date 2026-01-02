package com.github.s1maodyasz.machine.configuration;

import com.github.s1maodyasz.machine.util.NormalizerUtil;
import java.util.*;
import java.util.function.Function;

public abstract class AbstractConfigurationManager<V> implements ConfigurationManager<String, V> {

  private final Map<String, V> map = new HashMap<>();
  private final Function<V, String> extractor;

  protected AbstractConfigurationManager(Function<V, String> extractor) {
    this.extractor = extractor;
  }

  private static String k(String key) {
    return NormalizerUtil.normalize(key);
  }

  @Override
  public void register(V value) {
    register(extractor.apply(value), value);
  }

  @Override
  public void register(String key, V value) {
    map.put(k(key), value);
  }

  @Override
  public void unregister(String key) {
    map.remove(k(key));
  }

  @Override
  public boolean has(String key) {
    return map.containsKey(k(key));
  }

  @Override
  public Optional<V> get(String key) {
    return Optional.ofNullable(map.get(k(key)));
  }

  @Override
  public V require(String key) {
    final var value = map.get(k(key));
    if (value == null) throw new IllegalArgumentException("Unknown configuration: " + key);
    return value;
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
