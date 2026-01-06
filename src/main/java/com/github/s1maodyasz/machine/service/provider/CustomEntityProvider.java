package com.github.s1maodyasz.machine.service.provider;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

@FunctionalInterface
public interface CustomEntityProvider {

    Entity spawn(Location location, String model);
}
