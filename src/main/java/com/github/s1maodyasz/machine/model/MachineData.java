package com.github.s1maodyasz.machine.model;

import com.github.s1maodyasz.machine.model.enums.MachineUpgradeEnum;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class MachineData {

	@NonNull private final String key;

	private final Map<MachineUpgradeEnum, Integer> levels;

	private final double stack;
	private final double drops;
	private final double energy;

}
