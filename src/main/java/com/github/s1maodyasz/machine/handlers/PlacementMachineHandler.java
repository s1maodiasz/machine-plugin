package com.github.s1maodyasz.machine.handlers;

import com.github.s1maodyasz.machine.configuration.AbstractConfigurationManager;
import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import com.github.s1maodyasz.machine.model.MachineDisplayConfiguration;
import com.github.s1maodyasz.machine.provider.CustomEntityProvider;
import com.github.s1maodyasz.machine.util.ItemDataUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import static com.github.s1maodyasz.machine.handlers.MachinePlacementResult.*;

@RequiredArgsConstructor
public final class PlacementMachineHandler {

    private final @NotNull Gson gson;
    private final @NotNull NamespacedKey key;
    private final @NotNull CustomEntityProvider provider;
    private final @NotNull AbstractConfigurationManager<MachineConfiguration> configurationManager;

    public boolean isCandidate() {

    }

    public @NotNull MachinePlacementResult handle(
            @NotNull Player player, @NotNull Location location, @NotNull ItemStack stack) {
        final var pdc = ItemDataUtil.of(key);
        final var data = pdc.get(stack, PersistentDataType.STRING, s -> gson.fromJson(s, MachineData.class)).orElse(null);
        if (data == null)
            return new Ignore();

        final var key = data.key();
        final var configuration = configurationManager.get(key).orElse(null);

        // Case machine is deleted but player have the item with pdc key
        if (configuration == null)
            return new Ignore();

        final var display = configuration.display();
        if (display instanceof MachineDisplayConfiguration.Block block) {
            block.material()
        }

        return new Success();
    }
}
