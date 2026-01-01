package com.github.s1maodyasz.machine.service.provider;

import static com.github.s1maodyasz.machine.util.NormalizerUtil.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ItemProviderRegistry {

	private final Map<String, ItemProvider> providers = new HashMap<>();

	public @NotNull ItemProviderRegistry register(
			@NotNull String type, @NotNull ItemProvider provider) {
		providers.put(normalize(type), Objects.requireNonNull(provider, "provider"));
		return this;
	}

	public @NotNull ItemProviderRegistry unregister(@NotNull String type) {
		providers.remove(normalize(type));
		return this;
	}

	public boolean has(@NotNull String type) {
		return providers.containsKey(normalize(type));
	}

	public @Nullable ItemProvider getIfPresent(@NotNull String type) {
		return providers.get(normalize(type));
	}

	public @NotNull ItemProvider get(@NotNull String type) {
		final var provider = providers.get(normalize(type));
		if (provider == null)
			throw new IllegalArgumentException("No ItemProvider registered for type: " + type);
		return provider;
	}
}
