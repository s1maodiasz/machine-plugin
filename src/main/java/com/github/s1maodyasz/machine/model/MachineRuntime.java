package com.github.s1maodyasz.machine.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@Accessors(fluent = true)
public final class MachineRuntime {

    public static final MachineRuntime NOT_RUNNING = new MachineRuntime(0, 0);

    /** Time mark just to represent user collect drop or start machine */
    private long mark;

    /** End markup to represent the time */
    private long end;
}
