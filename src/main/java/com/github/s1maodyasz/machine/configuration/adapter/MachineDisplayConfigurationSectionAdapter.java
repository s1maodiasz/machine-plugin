package com.github.s1maodyasz.machine.configuration.adapter;

import com.github.s1maodyasz.machine.model.MachineDisplayConfiguration;
import com.github.s1maodyasz.machine.util.EntitiesUtil;
import com.github.s1maodyasz.machine.util.MaterialsUtil;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MachineDisplayConfigurationSectionAdapter {

	public static MachineDisplayConfiguration adapt(ConfigurationSection section) {
		Objects.requireNonNull(
				section, "Something going wrong adapting configuration section of display.");
		final var type = section.getString("type", "block");
		return type.equalsIgnoreCase("model") ? adaptModel(section) : adaptBlock(section);
	}

	private static MachineDisplayConfiguration.Block adaptBlock(ConfigurationSection section) {
		final var materialName = section.getString("material");
		final var material = MaterialsUtil.valueOf(materialName);
		return MachineDisplayConfiguration.Block.builder().material(material).build();
	}

	private static MachineDisplayConfiguration.Model adaptModel(ConfigurationSection section) {
		final var rawType = section.getString("entity-type", "INTERACTION");
		final var entityType = EntitiesUtil.valueOf(rawType);

		final var model = section.getString("model", null);
		if (model == null || model.isBlank())
			throw new IllegalArgumentException("display.type=model requires 'model' (BetterModel id).");

		return MachineDisplayConfiguration.Model.builder().type(entityType).model(model).build();
	}
}
