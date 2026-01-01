package com.github.s1maodyasz.machine.configuration.adapter;

import com.github.s1maodyasz.machine.model.TextConfiguration;

public final class TextConfigurationValueAdapter {

	public static TextConfiguration adapt(String text) {
		int index = 0;
		final var len = text.length();
		final var builder = TextConfiguration.builder().content(text);
		while (index < len) {
			int start = text.indexOf("{", index);
			if (start == -1) break;

			int end = text.indexOf("}", ++start);
			if (end == -1) break;

			final var placeholder = text.substring(start, end);

			index = end + 1;
			if (placeholder.isEmpty()) continue;

			builder.placeholder(placeholder);
		}

		return builder.build();
	}
}
