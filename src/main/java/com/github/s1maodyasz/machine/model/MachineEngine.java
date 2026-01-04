package com.github.s1maodyasz.machine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class MachineEngine {

    public static final MachineEngine NOT_RUNNING = new MachineEngine(false, 0);

    @Builder.Default
    @BsonProperty("active")
    private boolean active = false;

    @BsonProperty("lastInteractionAt")
    private long lastInteractionAt;

    public boolean isIdle() {
        return !active;
    }
}
