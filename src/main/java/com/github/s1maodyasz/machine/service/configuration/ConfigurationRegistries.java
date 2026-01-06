package com.github.s1maodyasz.machine.service.configuration;

import com.github.s1maodyasz.machine.service.configuration.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.service.configuration.model.MachineConfiguration;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder
public record ConfigurationRegistries(
    @NotNull ConfigurationRegistry<MachineConfiguration> machines,
    @NotNull ConfigurationRegistry<BatteryConfiguration> batteries) { }
