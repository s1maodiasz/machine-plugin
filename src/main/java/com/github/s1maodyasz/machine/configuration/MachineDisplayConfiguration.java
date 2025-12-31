package com.github.s1maodyasz.machine.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public interface MachineDisplayConfiguration {

    @Getter
    @Builder(toBuilder = true)
    final class Block implements MachineDisplayConfiguration {
        @Builder.Default
        @NonNull
        private Material material = Material.STONE;
    }

    @Getter
    @Builder(toBuilder = true)
    final class Model implements MachineDisplayConfiguration {
        @NonNull
        private EntityType type;
        @NonNull
        private String model;
    }
}
