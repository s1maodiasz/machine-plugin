package com.github.s1maodyasz.machine.util.formatter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeFormatter {

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum TimeUnit {
        WEEK(7 * 24 * 60 * 60, 'w'),
        DAY(24 * 60 * 60, 'd'),
        HOUR(60 * 60, 'h'),
        MINUTE(60, 'm'),
        SECOND(1, 's');

        private final long seconds;
        private final char symbol;
    }

    public static String format(long seconds) {
        if (seconds <= 0) return "0s";

        long s = seconds;

        final var b = new StringBuilder(24);

        for (final var u : TimeUnit.values()) {
            final long v = s / u.seconds;
            if (v <= 0) continue;

            s %= u.seconds;

            if (b.isEmpty()) b.append(v).append(u.symbol);
            else b.append(' ');
        }

        return b.isEmpty() ? "0s" : b.toString();
    }
}