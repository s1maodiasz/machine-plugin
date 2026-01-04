package com.github.s1maodyasz.machine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class BatterySlot {

    @NonNull
    @BsonProperty("key")
    private String key;

    @BsonProperty("total")
    private double total;

    @BsonProperty("activated")
    private boolean activated;

    @BsonProperty("insertionAt")
    private long insertionAt;

    public boolean deactivated() {
        return !activated;
    }
}
