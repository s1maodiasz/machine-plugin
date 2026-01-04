package com.github.s1maodyasz.machine.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@Accessors(fluent = true)
public abstract class Data {
    protected final String key;
}
