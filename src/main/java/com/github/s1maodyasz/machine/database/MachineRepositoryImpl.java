package com.github.s1maodyasz.machine.database;

import static com.mongodb.client.model.Filters.*;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MachineRepositoryImpl implements MachineRepository {

	private final @NotNull MongoCollection<Machine> collection;

	@Override
	public void upsert(@NotNull Machine machine) {
		collection.replaceOne(eq("_id", machine.getId()), machine, new ReplaceOptions().upsert(true));
	}

    @Override
    public void bulkUpsert(@NotNull List<Machine> machines) {

    }

    @Override
	public @NotNull Optional<Machine> findById(@NotNull UUID id) {
		return Optional.ofNullable(collection.find(eq("_id", id)).first());
	}

	@Override
	public @NotNull List<Machine> findByOwnerId(@NotNull UUID ownerId) {
		return collection.find(eq("ownerId", ownerId)).into(new ArrayList<>());
	}

	@Override
	public @NotNull Optional<Machine> findByLocation(@NotNull MachineLocation location) {
		return Optional.ofNullable(
				collection
						.find(
								and(
										eq("worldId", location.worldId()),
										eq("x", location.x()),
										eq("y", location.y()),
										eq("z", location.z())))
						.first());
	}

	@Override
	public void deleteById(@NotNull UUID id) {
		final DeleteResult result = collection.deleteOne(eq("_id", id));
		result.getDeletedCount();
	}
}
