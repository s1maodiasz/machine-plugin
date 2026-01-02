package com.github.s1maodyasz.machine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NormalizerUtil {

  public static String normalize(String key) {
    return key.trim().toLowerCase();
  }
}
