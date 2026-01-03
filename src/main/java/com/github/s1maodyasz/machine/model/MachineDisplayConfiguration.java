package com.github.s1maodyasz.machine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public interface MachineDisplayConfiguration {

    boolean isBlock();

    boolean isModel();

    @Getter
    @Builder(toBuilder = true)
    @Accessors(fluent = true)
    final class Block implements MachineDisplayConfiguration {
        @Builder.Default
        @NonNull
        private Material material = Material.STONE;

        @Override
        public boolean isBlock() {
            return true;
        }

        @Override
        public boolean isModel() {
            return false;
        }
    }

    @Getter
    @Builder(toBuilder = true)
    @Accessors(fluent = true)
    final class Model implements MachineDisplayConfiguration {
        @NonNull
        private EntityType type;

        @NonNull
        private String model;

        @Override
        public boolean isBlock() {
            return false;
        }

        @Override
        public boolean isModel() {
            return true;
        }
    }
}
