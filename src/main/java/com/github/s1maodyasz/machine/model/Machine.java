package com.github.s1maodyasz.machine.model;

import com.github.s1maodyasz.machine.model.enums.MachineUpgradeEnum;
import java.util.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.jetbrains.annotations.NotNull;

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
    private final String key;

    @NotNull
    @BsonProperty("ownerId")
    private UUID ownerId;

    @NonNull
    @BsonProperty("location")
    private MachineLocation location;

    @NonNull
    @BsonProperty("collaborators")
    @Builder.Default
    private final Set<MachineCollaborator> collaborators = new HashSet<>();

    @NonNull
    @BsonProperty("upgrades")
    @Builder.Default
    private final Map<MachineUpgradeEnum, Integer> upgrades = new EnumMap<>(MachineUpgradeEnum.class);

    @BsonProperty("runtime")
    @NonNull
    @Builder.Default
    private final MachineRuntime runtime = MachineRuntime.NOT_RUNNING;

    @Builder.Default
    @BsonProperty("stack")
    private double stack = 1;

    @Builder.Default
    @BsonProperty("drops")
    private double drops = 0;
}
