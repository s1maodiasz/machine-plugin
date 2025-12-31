package com.github.s1maodyasz.machine.configuration.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeyNormalizerUtil {

    public static String normalize(String key) {
        return key.trim().toLowerCase();
    }
}
