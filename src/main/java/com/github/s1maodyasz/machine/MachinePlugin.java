package com.github.s1maodyasz.machine;

import com.github.s1maodyasz.machine.database.MachineDatabase;
import com.github.s1maodyasz.machine.issuer.Issuers;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class MachinePlugin extends JavaPlugin {

    @Getter
    private static MachinePlugin instance;

    private MachineDatabase database;
    private Issuers issuers;

    @Override
    public void onLoad() {
        instance = this;

        saveDefaultConfig();

        database = new MachineDatabase(instance);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {
        database.close();
    }
}
