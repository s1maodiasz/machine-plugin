package com.github.s1maodyasz.machine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@Builder
public final class MachineLocation {

    @BsonProperty("worldId")
    @NonNull
    private UUID worldId;

    @BsonProperty("x")
    private long x;

    @BsonProperty("y")
    private long y;

    @BsonProperty("z")
    private long z;

}
