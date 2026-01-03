package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.configuration.AbstractConfigurationManager;
import com.github.s1maodyasz.machine.provider.CustomItemProvider;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class Issuer<C, D> {

    protected final Gson gson;
    protected final NamespacedKey namespacedKey;
    protected final CustomItemProvider provider;
    protected final AbstractConfigurationManager<C> configurationManager;

    public abstract IssueResult issue(@NotNull Player player, @NotNull String key, D data);
}
