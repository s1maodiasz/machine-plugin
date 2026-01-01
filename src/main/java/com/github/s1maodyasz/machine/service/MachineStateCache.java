package com.github.s1maodyasz.machine.service;

import com.github.s1maodyasz.machine.model.Machine;
import java.util.Collection;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

public interface MachineStateCache {

    void put(@NotNull Machine machine);

    @NotNull Collection<UUID> drain();

}