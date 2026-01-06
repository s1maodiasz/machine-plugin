package com.github.s1maodyasz.machine;

import com.github.s1maodyasz.machine.service.MachineService;
import com.github.s1maodyasz.machine.service.MachineServiceFactory;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class MachinePlugin extends JavaPlugin {

    @Getter
    private static MachinePlugin instance;

    private MachineService service;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        service = MachineServiceFactory.create(instance);
    }

    @Override
    public void onDisable() {
        service.shutdown();
    }
}
