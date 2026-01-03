package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;

public record Issuers(
        Issuer<MachineConfiguration, MachineData> machine, Issuer<MachineConfiguration, MachineData> battery) {}
