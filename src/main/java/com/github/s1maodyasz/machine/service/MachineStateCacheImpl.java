package com.github.s1maodyasz.machine.service;

import com.github.s1maodyasz.machine.model.Machine;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class MachineStateCacheImpl implements MachineStateCache {

    private final Set<UUID> states = new HashSet<>();

    @Override
    public void put(@NotNull Machine machine) {
        final var id = machine.getId();
        states.add(id);
    }

    @Override
    public @NotNull Collection<UUID> drain() {
        var collection = new ArrayList<>(states);
        states.clear();
        return collection;
    }
}
