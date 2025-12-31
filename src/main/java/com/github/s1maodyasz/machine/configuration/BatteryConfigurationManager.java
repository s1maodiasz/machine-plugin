package com.github.s1maodyasz.machine.configuration;

import com.github.s1maodyasz.machine.configuration.util.KeyNormalizerUtil;

public final class BatteryConfigurationManager extends AbstractConfigurationManager<BatteryConfiguration> {
    public BatteryConfigurationManager() {
        super(BatteryConfiguration::getKey, KeyNormalizerUtil::normalize);
    }
}