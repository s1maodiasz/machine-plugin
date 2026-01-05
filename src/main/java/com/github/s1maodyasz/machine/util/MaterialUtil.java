package com.github.s1maodyasz.machine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MaterialUtil {

    public static Material valueOf(String name) {
        final var material = Material.matchMaterial(name);
        if (material == null)
            throw new IllegalStateException("Minecraft material called " + name + " not exists");
        return material;
    }
}
