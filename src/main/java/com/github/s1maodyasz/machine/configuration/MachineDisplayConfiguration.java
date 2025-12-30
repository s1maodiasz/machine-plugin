package com.github.s1maodyasz.machine.configuration;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

public interface MachineDisplayConfiguration {

    @Getter
    @Builder(toBuilder = true)
    final class Block implements MachineDisplayConfiguration {
        private Material material;
    }

    @Getter
    @Builder(toBuilder = true)
    final class Model implements MachineDisplayConfiguration {
        private Entity entity;
        private String model;
    }
}
