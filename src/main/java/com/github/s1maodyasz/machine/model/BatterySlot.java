package com.github.s1maodyasz.machine.model;

import lombok.Data;

import lombok.NonNull;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
public class BatterySlot {

    @NonNull
    @BsonProperty("key")
    private String key;

    @BsonProperty("total")
    private double total;

    @BsonProperty("activated")
    private boolean activated;

    @BsonProperty("insertionAt")
    private long insertionAt;

    @BsonIgnore
    public boolean deactivated() {
        return !activated;
    }

}
