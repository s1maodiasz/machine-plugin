package com.github.s1maodyasz.machine.handlers;

import com.github.s1maodyasz.machine.configuration.AbstractConfigurationManager;
import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.provider.CustomEntityProvider;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class PlacementMachineHandler {

  private final @NotNull Gson gson;
  private final @NotNull NamespacedKey key; // key do payload
  private final @NotNull CustomEntityProvider provider;
  private final @NotNull AbstractConfigurationManager<MachineConfiguration> configuration;

  public @NotNull MachinePlacementResult handle(
      @NotNull Player player, @NotNull Location location, @NotNull ItemStack stack) {
    return MachinePlacementResult.SUCCESS;
  }
}
