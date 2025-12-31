package com.github.s1maodyasz.machine.provider;

import kr.toxicity.model.api.BetterModel;
import kr.toxicity.model.api.data.renderer.ModelRenderer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;

public final class BetterModelProvider implements CustomEntityProvider {

    @Override
    public Entity spawn(Location location, String model) {
        final ModelRenderer renderer = BetterModel.model(model).orElse(null);
        if (renderer == null)
            return null;

        final var entity = createDefaultEntity(location);
        renderer.getOrCreate(entity);
        return entity;
    }

    @Override
    public void detach(Entity entity) {
        entity.remove();
    }

    @Override
    public boolean hasModel(String model) {
        return false;
    }

    public Interaction createDefaultEntity(Location spawnLocation) {
        Interaction baseEntity = (Interaction) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ITEM_DISPLAY);
        baseEntity.setInvulnerable(true);
        baseEntity.setGravity(false);
        baseEntity.setCustomNameVisible(false);
        baseEntity.setPersistent(true);
        baseEntity.setSilent(true);
        baseEntity.setGlowing(false);
        baseEntity.setPersistent(true);
        return baseEntity;
    }
}
