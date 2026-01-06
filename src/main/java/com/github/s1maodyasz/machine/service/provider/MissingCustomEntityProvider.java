package com.github.s1maodyasz.machine.service.provider;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class MissingCustomEntityProvider implements CustomEntityProvider {

    @Override
    public Entity spawn(@NotNull Location location, @NotNull String model) {
        throw new IllegalStateException("CustomEntityProvider not available");
    }
}
