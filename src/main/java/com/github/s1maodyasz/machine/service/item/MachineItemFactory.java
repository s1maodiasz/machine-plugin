package com.github.s1maodyasz.machine.service.item;

import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.service.item.pipeline.MachineItemContext;
import com.github.s1maodyasz.machine.service.item.pipeline.MachineItemPipeline;
import com.github.s1maodyasz.machine.util.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class MachineItemFactory {

    private final @NotNull MachineItemPipeline pipeline;

    public @NotNull ItemStack create(@NotNull MachineConfiguration configuration, @NotNull MachineData data) {
        return pipeline.build(
            new MachineItemContext(
                configuration,
                data,
                ItemBuilder.of(new ItemStack(Material.STONE))
            )
        );
    }
}
