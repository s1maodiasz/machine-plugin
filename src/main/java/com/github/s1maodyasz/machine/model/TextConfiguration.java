package com.github.s1maodyasz.machine.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class TextConfiguration {

	@NonNull private String content;

	@Singular @NonNull private List<String> placeholders;
}
