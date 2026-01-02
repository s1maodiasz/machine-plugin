package com.github.s1maodyasz.machine.provider;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class MissingCustomItemProvider implements CustomItemProvider {

  @Override
  public @NotNull ItemStack resolve(@NotNull String value) {
    throw new IllegalStateException(
        "CustomItemProvider not available, need to add Nexo for example.");
  }
}
