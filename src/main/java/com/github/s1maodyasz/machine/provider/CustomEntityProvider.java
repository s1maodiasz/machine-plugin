package com.github.s1maodyasz.machine.provider;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

@FunctionalInterface
public interface CustomEntityProvider {

    Entity spawn(Location location, String model);
}
