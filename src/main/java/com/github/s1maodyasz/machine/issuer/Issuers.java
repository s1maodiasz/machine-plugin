package com.github.s1maodyasz.machine.issuer;

import com.github.s1maodyasz.machine.model.MachineConfiguration;
import com.github.s1maodyasz.machine.model.MachineData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

public record Issuers(Issuer<MachineConfiguration, MachineData> machine, Issuer<MachineConfiguration, MachineData> battery) {
}
