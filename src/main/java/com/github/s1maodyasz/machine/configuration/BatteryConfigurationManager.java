package com.github.s1maodyasz.machine.configuration;

import com.github.s1maodyasz.machine.configuration.util.KeyNormalizerUtil;

public final class EnergyConfigurationManager extends AbstractConfigurationManager<BatteryConfiguration> {
    public EnergyConfigurationManager() {
        super(BatteryConfiguration::getKey, KeyNormalizerUtil::normalize);
    }
}