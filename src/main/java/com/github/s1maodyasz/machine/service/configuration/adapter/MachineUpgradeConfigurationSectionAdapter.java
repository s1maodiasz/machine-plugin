package com.github.s1maodyasz.machine.service.configuration.adapter;

import com.github.s1maodyasz.machine.model.MachineUpgradeConfiguration;
import java.util.Objects;
import org.bukkit.configuration.ConfigurationSection;

public final class MachineUpgradeConfigurationSectionAdapter {

	public static MachineUpgradeConfiguration adapt(final ConfigurationSection section) {
		Objects.requireNonNull(section, "section");

		final var base = section.getDouble("base");
		if (base <= 0) throw new IllegalStateException("Modifier of Machine Upgrade cannot be null.");

		final var modifier = section.getDouble("modifier");
		if (modifier <= 0)
			throw new IllegalStateException("Modifier of Machine Upgrade cannot be null.");

		final var max = section.getInt("max");
		if (max <= 0) throw new IllegalStateException("Maximum of Machine Upgrade cannot be null.");

		return MachineUpgradeConfiguration.builder().base(base).modifier(modifier).max(max).build();
	}
}
