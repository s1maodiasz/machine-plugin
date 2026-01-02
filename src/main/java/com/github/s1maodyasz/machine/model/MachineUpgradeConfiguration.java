package com.github.s1maodyasz.machine.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class MachineUpgradeConfiguration {

  private double base;
  private double modifier;
  private int max;

  public double valueAt(int level) {
    final int l = Math.min(level, max);
    return base + (modifier * l);
  }
}
