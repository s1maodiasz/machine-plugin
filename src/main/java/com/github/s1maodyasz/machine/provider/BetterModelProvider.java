package com.github.s1maodyasz.machine.provider;

import kr.toxicity.model.api.BetterModel;
import kr.toxicity.model.api.data.renderer.ModelRenderer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;

/** They stay always on same position */
public final class BetterModelProvider implements CustomEntityProvider {

  @Override
  public Entity spawn(Location location, String model) {
    final ModelRenderer renderer = BetterModel.model(model).orElse(null);
    if (renderer == null) return null;

    final var entity = createDefaultEntity(location);
    renderer.getOrCreate(entity);
    return entity;
  }

  private Interaction createDefaultEntity(Location location) {
    Interaction baseEntity =
        (Interaction) location.getWorld().spawnEntity(location, EntityType.INTERACTION);
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
