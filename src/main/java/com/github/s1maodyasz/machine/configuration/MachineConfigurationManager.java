package com.github.s1maodyasz.machine.configuration;

import com.github.s1maodyasz.machine.configuration.util.KeyNormalizerUtil;

public final class MachineConfigurationManager extends AbstractConfigurationManager<MachineConfiguration> {

    public MachineConfigurationManager() {
        super(MachineConfiguration::getKey, KeyNormalizerUtil::normalize);
    }
}
