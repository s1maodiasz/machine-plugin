package com.github.s1maodyasz.machine.service.provider;

import kr.toxicity.model.api.BetterModel;
import kr.toxicity.model.api.data.renderer.ModelRenderer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;

/** They stay always on same position */
public final class BetterModelProvider implements EntityProvider {

	@Override
	public Entity spawn(Location location, String model) {
		final ModelRenderer renderer = BetterModel.model(model).orElse(null);
		if (renderer == null) return null;

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
