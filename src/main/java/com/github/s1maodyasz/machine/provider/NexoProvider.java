package com.github.s1maodyasz.machine.provider;

import com.github.s1maodyasz.machine.util.ItemBuilder;
import com.nexomc.nexo.api.NexoItems;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public final class NexoProvider implements CustomItemProvider {

  @Override
  public @NonNull ItemStack resolve(@NotNull String value) {
    final var builder = NexoItems.itemFromId(value);
    return builder == null ? ItemBuilder.NONE : builder.build();
  }
}
