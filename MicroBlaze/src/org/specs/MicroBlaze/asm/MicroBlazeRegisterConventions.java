package org.specs.MicroBlaze.asm;

import pt.up.fe.specs.binarytranslation.asm.AIsaRegisterConventions;

public class MicroBlazeRegisterConventions extends AIsaRegisterConventions {

    public MicroBlazeRegisterConventions() {
        setAllRegisters("r0", "r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8", "r9", "r10", 
                        "r11", "r12", "r13", "r14", "r15", "r16", "r17", "r18", "r19", "r20",
                        "r21", "r22", "r23", "r24", "r25", "r26", "r27", "r28", "r29", "r30",
                        "r31", "rpc");
        setParameters("r5", "r6", "r7", "r8", "r9", "r10");
        setTemporaries("r3", "r4", "r5", "r6", "r7", "r8", "r9", "r10", "r11", "r12");
        setReturnVals("r3", "r4");
        setStackPointer("r1");
        setZero("r0");
    }
}
