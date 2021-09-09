package org.specs.MicroBlaze.asm;

import static pt.up.fe.specs.binarytranslation.instruction.register.RegisterType.*;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.register.Register;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterType;

public enum MicroBlazeRegister implements Register {

    RO("r0", HARDZERO),
    R1("r1", STACKPOINTER),
    R2("r2", GENERALPURPOSE),
    R3("r3", GENERALPURPOSE, TEMPORARY, RETURN),
    R4("r4", GENERALPURPOSE, TEMPORARY, RETURN),
    R5("r5", GENERALPURPOSE, TEMPORARY, PARAMETER),
    R6("r6", GENERALPURPOSE, TEMPORARY, PARAMETER),
    R7("r7", GENERALPURPOSE, TEMPORARY, PARAMETER),
    R8("r8", GENERALPURPOSE, TEMPORARY, PARAMETER),
    R9("r9", GENERALPURPOSE, TEMPORARY, PARAMETER),
    R10("r10", GENERALPURPOSE, TEMPORARY, PARAMETER),
    R11("r11", GENERALPURPOSE, TEMPORARY),
    R12("r12", GENERALPURPOSE, TEMPORARY),
    R13("r13", GENERALPURPOSE),
    R14("r14", GENERALPURPOSE),
    R15("r15", GENERALPURPOSE),
    R16("r16", GENERALPURPOSE),
    R17("r17", GENERALPURPOSE),
    R18("r18", GENERALPURPOSE),
    R19("r19", GENERALPURPOSE),
    R20("r20", GENERALPURPOSE),
    R21("r21", GENERALPURPOSE),
    R22("r22", GENERALPURPOSE),
    R23("r23", GENERALPURPOSE),
    R24("r24", GENERALPURPOSE),
    R25("r25", GENERALPURPOSE),
    R26("r26", GENERALPURPOSE),
    R27("r27", GENERALPURPOSE),
    R28("r28", GENERALPURPOSE),
    R29("r29", GENERALPURPOSE),
    R30("r30", GENERALPURPOSE),
    R31("r31", GENERALPURPOSE),
    RPC("rpc", PROGRAMCOUNTER);

    private final String name;
    private final List<RegisterType> regTypes;

    private MicroBlazeRegister(String name, RegisterType... registerTypes) {
        this.name = name;
        this.regTypes = Arrays.asList(registerTypes);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<RegisterType> getRegTypes() {
        return regTypes;
    }
}
