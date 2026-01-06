package com.github.s1maodyasz.machine.service.provider;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public final class NexoProvider implements CustomItemProvider {

    @Override
    public @NonNull Optional<ItemStack> resolve(@NotNull String value) {
        final var builder = NexoItems.itemFromId(value);
        if (builder == null) return Optional.empty();
        final var item = builder.build();
        return Optional.of(item);
    }
}
