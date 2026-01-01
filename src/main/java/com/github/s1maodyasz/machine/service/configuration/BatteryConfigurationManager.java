package com.github.s1maodyasz.machine.service.configuration;

import com.github.s1maodyasz.machine.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.util.NormalizerUtil;

public final class BatteryConfigurationManager
		extends AbstractConfigurationManager<BatteryConfiguration> {
	public BatteryConfigurationManager() {
		super(BatteryConfiguration::getKey, NormalizerUtil::normalize);
	}
}
