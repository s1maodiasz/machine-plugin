package com.github.s1maodyasz.machine.configuration;

import com.github.s1maodyasz.machine.model.BatteryConfiguration;

public final class BatteryConfigurationManager extends AbstractConfigurationManager<BatteryConfiguration> {
    public BatteryConfigurationManager() {
        super(BatteryConfiguration::key);
    }
}
