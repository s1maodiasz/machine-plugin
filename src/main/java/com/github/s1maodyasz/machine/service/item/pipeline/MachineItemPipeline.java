package com.github.s1maodyasz.machine.service.item.pipeline;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MachineItemPipeline {

	private final @NotNull List<MachineItemDecorator> decorators;

	public @NotNull ItemStack build(@NotNull MachineItemContext ctx) {
		decorators.forEach(d -> d.decorate(ctx));
		return ctx.builder().build();
	}
}
