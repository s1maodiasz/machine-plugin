package com.github.s1maodyasz.machine.model;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public final class ItemConfiguration {

  @NonNull private String value;

  private String model;

  @NonNull private TextConfiguration name;

  @Builder.Default private List<TextConfiguration> lore = Collections.emptyList();

  @Builder.Default private boolean unbreakable = false;
}
