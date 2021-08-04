package org.specs.MicroBlaze;

import org.specs.MicroBlaze.asm.MicroBlazeApplication;

import pt.up.fe.specs.binarytranslation.processes.QEMU;

public class MicroBlazeQEMU extends QEMU {

    public MicroBlazeQEMU(MicroBlazeApplication app) {
        super(app);
    }
}
