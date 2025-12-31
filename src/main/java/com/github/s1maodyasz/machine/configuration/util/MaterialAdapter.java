package com.github.s1maodyasz.machine.configuration.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MaterialAdapter {

    public static Material adapt(String name) {
        final var material = Material.matchMaterial(name);
        return material == null ? Material.STONE : material;
    }
}
