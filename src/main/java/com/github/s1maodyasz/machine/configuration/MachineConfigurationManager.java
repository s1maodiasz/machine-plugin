package com.github.s1maodyasz.machine.configuration;

import com.github.s1maodyasz.machine.model.MachineConfiguration;

public final class MachineConfigurationManager extends AbstractConfigurationManager<MachineConfiguration> {

    public MachineConfigurationManager() {
        super(MachineConfiguration::key);
    }
}
