package com.github.s1maodyasz.machine.service.provider;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemProvider {

	@NotNull
	ItemStack resolve(@NotNull String value);
}
