package com.github.s1maodyasz.machine.model;

import com.github.s1maodyasz.machine.configuration.model.ItemConfiguration;
import org.jetbrains.annotations.NotNull;

public interface ItemConfigurable {

    @NotNull
    ItemConfiguration item();

}
