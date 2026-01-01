package com.github.s1maodyasz.machine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.EntityType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntitiesUtil {

	public static EntityType valueOf(String type) {
		try {
			final var upper = type.toUpperCase();
			return EntityType.valueOf(upper);
		} catch (Exception e) {
			throw new IllegalStateException("Entity " + type + " not exists.");
		}
	}
}
