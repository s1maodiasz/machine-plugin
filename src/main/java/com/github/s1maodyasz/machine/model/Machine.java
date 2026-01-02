package com.github.s1maodyasz.machine.model;

import java.util.*;

import com.github.s1maodyasz.machine.model.types.MachineUpgradeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
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
    private final Set<MachineCollaborator> collaborators = new HashSet<>();

    @Singular
    @NonNull
    @BsonProperty("upgrades")
    private final Map<MachineUpgradeEnum, Integer> upgrades = new EnumMap<>(MachineUpgradeEnum.class);

    @BsonProperty("runtime")
    @NonNull
    private final MachineRuntime runtime;

    @Builder.Default
    @BsonProperty("stack")
    private double stack = 1;

    @Builder.Default
    @BsonProperty("drops")
    private double drops = 0;
}
