package com.github.s1maodyasz.machine.database;

import static com.mongodb.client.model.Filters.*;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MachineRepository {

  private final @NotNull MongoCollection<Machine> collection;

  public void upsert(@NotNull Machine machine) {
    collection.replaceOne(eq("_id", machine.id()), machine, new ReplaceOptions().upsert(true));
  }

  public void upsertAll(@NotNull List<Machine> machines) {
    if (machines.isEmpty()) return;

    final var upsertOptions = new ReplaceOptions().upsert(true);
    final List<WriteModel<Machine>> ops = new ArrayList<>(machines.size());
    for (var machine : machines) {
      ops.add(new ReplaceOneModel<>(eq("_id", machine.id()), machine, upsertOptions));
    }

    collection.bulkWrite(ops, new BulkWriteOptions().ordered(false));
  }

  public @NotNull Optional<Machine> findById(@NotNull UUID id) {
    return Optional.ofNullable(collection.find(eq("_id", id)).first());
  }

  public @NotNull List<Machine> findByOwnerId(@NotNull UUID ownerId) {
    return collection.find(eq("ownerId", ownerId)).into(new ArrayList<>());
  }

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

  public void deleteById(@NotNull UUID id) {
    final DeleteResult result = collection.deleteOne(eq("_id", id));
    result.getDeletedCount();
  }
}
