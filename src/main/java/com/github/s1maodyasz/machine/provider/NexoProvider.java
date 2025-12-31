package com.github.s1maodyasz.machine.provider;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NexoProvider implements CustomItemProvider {

    @Override
    public @Nullable ItemStack resolve(@NotNull String id) {
        final var builder = NexoItems.itemFromId(id);
        return builder == null ? null : builder.build();
    }
}
