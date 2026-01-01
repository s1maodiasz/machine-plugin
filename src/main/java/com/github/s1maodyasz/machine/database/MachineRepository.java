package com.github.s1maodyasz.machine.database;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface MachineRepository {

	void save(@NotNull Machine machine);

	@NotNull
	Optional<Machine> findById(@NotNull UUID id);

	@NotNull
	List<Machine> findByOwnerId(@NotNull UUID ownerId);

	@NotNull
	Optional<Machine> findByLocation(@NotNull MachineLocation location);

	void deleteById(@NotNull UUID id);
}
