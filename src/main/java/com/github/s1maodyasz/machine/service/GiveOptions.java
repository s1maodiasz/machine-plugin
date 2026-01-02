package com.github.s1maodyasz.machine.service;

import lombok.Builder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Builder
public final class GiveOptions {

  public enum Purpose {
    MACHINE,
    BATTERY
  }

  @NotNull Player player;

  @NotNull String key;

  @NotNull Purpose purpose;

  @Builder.Default double amount = 1;
}
