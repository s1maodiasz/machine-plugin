package com.github.s1maodyasz.machine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@AllArgsConstructor
public class MachineEngine {

    public static final MachineEngine NOT_RUNNING = new MachineEngine(false, 0);

    @BsonProperty("active")
    private boolean active = false;

    @BsonProperty("lastInteractionAt")
    private long lastInteractionAt;

    @BsonIgnore
    public boolean isIdle() {
        return !active;
    }

    @BsonIgnore
    public void setLastInteractionNow() {
        this.lastInteractionAt = System.currentTimeMillis();
    }
}
