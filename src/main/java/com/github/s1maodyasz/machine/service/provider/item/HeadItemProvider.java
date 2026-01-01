package com.github.s1maodyasz.machine.service.provider.item;

import com.github.s1maodyasz.machine.service.provider.ItemProvider;
import com.github.s1maodyasz.machine.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class HeadItemProvider implements ItemProvider {

	@Override
	public @NotNull ItemStack resolve(@NotNull String value) {
		final var builder = ItemBuilder.of(Material.PLAYER_HEAD);
		return value.isEmpty()
				? builder.build()
				: builder.skullTextureUrl("https://textures.minecraft.net/texture/" + value).build();
	}
}
