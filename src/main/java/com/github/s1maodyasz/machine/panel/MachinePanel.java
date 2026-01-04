package com.github.s1maodyasz.machine.panel;

import com.github.s1maodyasz.machine.model.Machine;
import com.github.s1maodyasz.machine.model.MachineConfiguration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MachinePanel implements Panel {

    protected final Machine machine;
    protected final MachineConfiguration configuration;

}
