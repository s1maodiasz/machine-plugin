package com.github.s1maodyasz.machine.service.database;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mongodb.client.model.Filters.*;

@RequiredArgsConstructor
public final class MachineRepository {

    private final MongoCollection<Machine> collection;

    public void merge(Machine machine) {
        collection.replaceOne(
            eq("_id", machine.getId()),
            machine,
            new ReplaceOptions().upsert(true)
        );
    }

    public Optional<Machine> findById(UUID id) {
        return Optional.ofNullable(
            collection.find(eq("_id", id)).first()
        );
    }

    public List<Machine> findByOwnerId(UUID ownerId) {
        return collection
            .find(eq("ownerId", ownerId))
            .into(new ArrayList<>());
    }

    public Optional<Machine> findByLocation(MachineLocation location) {
        return Optional.ofNullable(
            collection.find(and(
                eq("worldId", location.getWorldId()),
                eq("x", location.getX()),
                eq("y", location.getY()),
                eq("z", location.getZ())
            )).first()
        );
    }

    public void deleteById(UUID id) {
        collection.deleteOne(eq("_id", id));
    }
}
