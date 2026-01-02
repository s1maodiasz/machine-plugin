package com.github.s1maodyasz.machine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MaterialsUtil {

  public static Material valueOf(String name) {
    final var material = Material.matchMaterial(name);
    return material == null ? Material.STONE : material;
  }
}
