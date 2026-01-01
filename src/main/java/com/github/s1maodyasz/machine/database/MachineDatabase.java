package com.github.s1maodyasz.machine.database;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineLocation;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

public interface MachineDatabase extends AutoCloseable {

    @NotNull Optional<Machine> findByIdSync(@NotNull UUID id);

    @NotNull CompletableFuture<Optional<Machine>> findByIdAsync(@NotNull UUID id);

    @NotNull Optional<Machine> findByLocationSync(@NotNull MachineLocation location);

    @NotNull CompletableFuture<Optional<Machine>> findByLocationAsync(@NotNull MachineLocation location);

    void saveSync(@NotNull Machine machine);

    @NotNull CompletableFuture<Void> saveAsync(@NotNull Machine machine);

    void removeSync(@NotNull Machine machine);

    @NotNull CompletableFuture<Void> removeAsync(@NotNull Machine machine);

    @NotNull CompletableFuture<Void> saveAllAsync(@NotNull Collection<Machine> machines);

    @NotNull CompletableFuture<Void> removeAllAsync(@NotNull Collection<UUID> ids);

    @Override
    void close();

}