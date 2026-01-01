package com.github.s1maodyasz.machine.service.item.pipeline;

import com.github.s1maodyasz.machine.model.MachineData;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MachinePersistDataDecorator implements MachineItemDecorator {

    private final @NotNull NamespacedKey machineDataKey;
    private final @NotNull Gson gson;

    @Override
    public void decorate(@NotNull MachineItemContext ctx) {
        final MachineData data = ctx.data();
        final String payload = gson.toJson(data);

        ctx.builder()
            .pdc()
            .set(machineDataKey, PersistentDataType.STRING, payload);
    }
}
