package com.github.s1maodyasz.machine.service.item.pipeline;

import org.jetbrains.annotations.NotNull;

public interface MachineItemDecorator {

	void decorate(@NotNull MachineItemContext ctx);
}
