package com.github.s1maodyasz.machine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public abstract class StackableData {

    protected double stack;

}
