package com.github.s1maodyasz.machine.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder(toBuilder = true)
public final class BatteryConfiguration {

    @NonNull
    private String key;

    @NonNull
    private ItemConfiguration item;

    private double amount;

}
