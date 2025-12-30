package com.github.s1maodyasz.machine.model;

import com.github.s1maodyasz.machine.model.enums.MachineUpgradeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Builder(toBuilder = true)
public final class Machine {

    @BsonId
    @Builder.Default
    @NonNull
    private final UUID id = UUID.randomUUID();

    @NonNull
    @BsonProperty("key")
    private final UUID key;

    @NonNull
    @BsonProperty("location")
    private MachineLocation location;

    @Singular
    @NonNull
    @BsonProperty("collaborators")
    private final List<MachineCollaborator> collaborators = new CopyOnWriteArrayList<>();

    @Singular
    @NonNull
    @BsonProperty("upgrades")
    private final Map<MachineUpgradeEnum, Integer> upgrades = new HashMap<>();

    @Builder.Default
    @BsonProperty("energy")
    private double energy = 0;

    @Builder.Default
    @BsonProperty("stack")
    private double stack = 1;

    @Builder.Default
    @BsonProperty("drops")
    private double drops = 0;

}
