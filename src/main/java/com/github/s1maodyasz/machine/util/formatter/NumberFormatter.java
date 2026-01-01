package com.github.s1maodyasz.machine.util.formatter;

import java.text.DecimalFormat;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class NumberFormatter {

	private static final String NEGATIVE_SYMBOL = "-";

	private static final List<String> SUFFIXES;
	private static final DecimalFormat DECIMAL_FORMAT;

	static {
		SUFFIXES = List.of("K", "M", "B", "T", "Q", "QQ", "S", "SS");
		DECIMAL_FORMAT = new DecimalFormat("#.###");
	}

	public static @NotNull String format(final double value) {
		var absolute = Math.abs(value);
		if (absolute < 1000) return DECIMAL_FORMAT.format(value);
		var index = 0;
		while ((absolute /= 1000.0) >= 1.0 && index <= SUFFIXES.size() - 1) index++;
		final var built = DECIMAL_FORMAT.format(absolute) + SUFFIXES.get(index);
		return value < 0 ? NEGATIVE_SYMBOL + built : built;
	}
}
