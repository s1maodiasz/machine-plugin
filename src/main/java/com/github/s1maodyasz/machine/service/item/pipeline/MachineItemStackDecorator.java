package com.github.s1maodyasz.machine.service.item.pipeline;

import com.github.s1maodyasz.machine.service.provider.ItemProviderRegistry;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MachineItemStackDecorator implements MachineItemDecorator {

	private final @NotNull ItemProviderRegistry registry;

	@Override
	public void decorate(@NotNull MachineItemContext ctx) {
		final var item = ctx.configuration().item();
		ctx.builder().stack(registry.get(item.type()).resolve(item.value()));
	}
}
