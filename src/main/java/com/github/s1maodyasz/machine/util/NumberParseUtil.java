package com.github.s1maodyasz.machine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberParseUtil {

    public static Integer requireNotZero(String value, String message) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Cannot parse value '" + value + "'");
        }
    }
}
