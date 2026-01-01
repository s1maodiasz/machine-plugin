package com.github.s1maodyasz.machine.service.configuration;

import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.util.NormalizerUtil;

public final class MachineConfigurationManager
		extends AbstractConfigurationManager<MachineConfiguration> {

	public MachineConfigurationManager() {
		super(MachineConfiguration::getKey, NormalizerUtil::normalize);
	}
}
