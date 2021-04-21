package org.specs.MicroBlaze.asm;

import pt.up.fe.specs.binarytranslation.asm.AIsaRegisterConventions;

public class MicroBlazeRegisterConventions extends AIsaRegisterConventions {

    public MicroBlazeRegisterConventions() {
        setParameters("r5", "r6", "r7", "r8", "r9", "r10");
        setTemporaries("r3", "r4", "r5", "r6", "r7", "r8", "r9", "r10", "r11", "r12");
        setReturnVals("r3", "r4");
        setStackPointer("r1");
        setZero("r0");
    }
}

