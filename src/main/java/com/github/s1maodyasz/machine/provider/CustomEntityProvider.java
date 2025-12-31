package com.github.s1maodyasz.machine.provider;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface CustomEntityProvider {

    Entity spawn(Location location, String model);

    void detach(Entity entity);

    boolean hasModel(String model);

}
