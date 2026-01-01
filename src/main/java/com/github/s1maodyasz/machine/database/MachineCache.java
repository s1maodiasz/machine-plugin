package com.github.s1maodyasz.machine.database;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface MachineCache {

	void put(@NotNull Machine machine);

	Optional<Machine> getByLocation(@NotNull MachineLocation location);

	void removeByLocation(@NotNull MachineLocation location);

	@NotNull
	Map<MachineLocation, Machine> getAll();
}
