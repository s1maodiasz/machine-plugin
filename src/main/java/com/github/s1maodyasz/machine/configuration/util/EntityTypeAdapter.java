package com.github.s1maodyasz.machine.configuration.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.EntityType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityTypeAdapter {

    public static EntityType adapt(String type) {
        try {
            final var upper = type.toUpperCase();
            return EntityType.valueOf(upper);
        } catch (Exception e) {
            return EntityType.INTERACTION;
        }
    }
}
