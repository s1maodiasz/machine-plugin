package com.github.s1maodyasz.machine.model;

import java.util.UUID;

import lombok.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@AllArgsConstructor
public class MachineLocation {

    @BsonProperty("worldId")
    private UUID worldId;

    @BsonProperty("x")
    private int x;

    @BsonProperty("y")
    private int y;

    @BsonProperty("z")
    private int z;
}
