package com.github.s1maodyasz.machine;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class MachinePlugin extends JavaPlugin {

    @Getter
    private static MachinePlugin instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
