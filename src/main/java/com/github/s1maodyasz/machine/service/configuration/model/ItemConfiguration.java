package com.github.s1maodyasz.machine.service.configuration.model;

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

    @NonNull
    private String material;

    private String url;

    private String model;

    @NonNull
    private String name;

    @Builder.Default
    private List<String> lore = Collections.emptyList();

    @Builder.Default
    private boolean unbreakable = false;
}
