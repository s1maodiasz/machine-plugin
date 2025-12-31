package com.github.s1maodyasz.machine.configuration;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public final class ItemConfiguration {

    @Builder.Default
    private Material material = Material.STONE;

    private String name;

    @Builder.Default
    private List<String> lore = Collections.emptyList();

    @Builder.Default
    private Integer customModelData = null;

    @Builder.Default
    private String modelId = null;

    @Builder.Default
    private boolean unbreakable = false;

}
