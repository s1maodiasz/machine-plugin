package com.github.s1maodyasz.machine.service.item;

import com.github.s1maodyasz.machine.service.configuration.model.BatteryConfiguration;
import com.github.s1maodyasz.machine.service.configuration.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.BatteryData;
import com.github.s1maodyasz.machine.model.MachineData;
import org.jetbrains.annotations.NotNull;

public record ItemFactories(
    @NotNull ItemFactory<MachineConfiguration, MachineData> machines,
    @NotNull ItemFactory<BatteryConfiguration, BatteryData> batteries) { }
