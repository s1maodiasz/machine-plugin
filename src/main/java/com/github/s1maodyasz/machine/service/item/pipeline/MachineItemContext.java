package com.github.s1maodyasz.machine.service.item.pipeline;

import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.util.ItemBuilder;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

public record MachineItemContext(
		@NotNull MachineConfiguration configuration,
		@NotNull MachineData data,
		@NotNull ItemBuilder builder) {}
