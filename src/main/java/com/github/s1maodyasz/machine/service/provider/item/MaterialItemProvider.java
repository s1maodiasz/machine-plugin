package com.github.s1maodyasz.machine.service.provider.item;

import com.github.s1maodyasz.machine.service.provider.ItemProvider;
import com.github.s1maodyasz.machine.util.ItemBuilder;
import com.github.s1maodyasz.machine.util.MaterialsUtil;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public final class MaterialItemProvider implements ItemProvider {

	@Override
	public @NonNull ItemStack resolve(@NotNull String value) {
		final var material = MaterialsUtil.valueOf(value);
		return ItemBuilder.of(material).build();
	}
}
