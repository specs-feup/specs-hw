package org.specs.Riscv.asm;

import static pt.up.fe.specs.binarytranslation.instruction.register.RegisterType.*;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.register.Register;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterType;

public enum RiscvRegister implements Register {

    // GP bank
    x0("zero", HARDZERO),
    x1("ra", GENERALPURPOSE, RETURNADDR),
    x2("sp", STACKPOINTER),
    x3("gp", GENERALPURPOSE, GLOBALPOINTER),
    x4("tp", GENERALPURPOSE, THREADPOINTER),
    x5("t0", GENERALPURPOSE, TEMPORARY),
    x6("t1", GENERALPURPOSE, TEMPORARY),
    x7("t2", GENERALPURPOSE, TEMPORARY),
    x8("fp", GENERALPURPOSE, TEMPORARY), // TODO this register has an alias "s0"
    x9("s1", GENERALPURPOSE, SAVED),
    x10("a0", GENERALPURPOSE, PARAMETER, RETURNVALUE),
    x11("a1", GENERALPURPOSE, PARAMETER, RETURNVALUE),
    x12("a2", GENERALPURPOSE, PARAMETER),
    x13("a3", GENERALPURPOSE, PARAMETER),
    x14("a4", GENERALPURPOSE, PARAMETER),
    x15("a5", GENERALPURPOSE, PARAMETER),
    x16("a6", GENERALPURPOSE, PARAMETER),
    x17("a7", GENERALPURPOSE, PARAMETER),
    x18("s2", GENERALPURPOSE, SAVED),
    x19("s3", GENERALPURPOSE, SAVED),
    x20("s4", GENERALPURPOSE, SAVED),
    x21("s5", GENERALPURPOSE, SAVED),
    x22("s6", GENERALPURPOSE, SAVED),
    x23("s7", GENERALPURPOSE, SAVED),
    x24("s8", GENERALPURPOSE, SAVED),
    x25("s9", GENERALPURPOSE, SAVED),
    x26("s10", GENERALPURPOSE, SAVED),
    x27("s11", GENERALPURPOSE, SAVED),
    x28("t3", GENERALPURPOSE, TEMPORARY),
    x29("t4", GENERALPURPOSE, TEMPORARY),
    x30("t5", GENERALPURPOSE, TEMPORARY),
    x31("t6", GENERALPURPOSE, TEMPORARY),
    f0("ft0", GENERALPURPOSE, TEMPORARY), // TODO: add FLOAT??... currently this info is encoded as an OperandDataType
    f1("ft1", GENERALPURPOSE, TEMPORARY),
    f2("ft2", GENERALPURPOSE, TEMPORARY),
    f3("ft3", GENERALPURPOSE, TEMPORARY),
    f4("ft4", GENERALPURPOSE, TEMPORARY),
    f5("ft5", GENERALPURPOSE, TEMPORARY),
    f6("ft6", GENERALPURPOSE, TEMPORARY),
    f7("ft7", GENERALPURPOSE, TEMPORARY),
    f8("fs0", GENERALPURPOSE, SAVED),
    f9("fs1", GENERALPURPOSE, SAVED),
    f10("fa0", GENERALPURPOSE, PARAMETER, RETURNVALUE),
    f11("fa1", GENERALPURPOSE, PARAMETER, RETURNVALUE),
    f12("fa2", GENERALPURPOSE, PARAMETER),
    f13("fa3", GENERALPURPOSE, PARAMETER),
    f14("fa4", GENERALPURPOSE, PARAMETER),
    f15("fa5", GENERALPURPOSE, PARAMETER),
    f16("fa6", GENERALPURPOSE, PARAMETER),
    f17("fa7", GENERALPURPOSE, PARAMETER),
    f18("fs2", GENERALPURPOSE, SAVED),
    f19("fs3", GENERALPURPOSE, SAVED),
    f20("fs4", GENERALPURPOSE, SAVED),
    f21("fs5", GENERALPURPOSE, SAVED),
    f22("fs6", GENERALPURPOSE, SAVED),
    f23("fs7", GENERALPURPOSE, SAVED),
    f24("fs8", GENERALPURPOSE, SAVED),
    f25("fs9", GENERALPURPOSE, SAVED),
    f26("fs10", GENERALPURPOSE, SAVED),
    f27("fs11", GENERALPURPOSE, SAVED),
    f28("ft8", GENERALPURPOSE, TEMPORARY),
    f29("ft9", GENERALPURPOSE, TEMPORARY),
    f30("ft10", GENERALPURPOSE, TEMPORARY),
    f31("ft11", GENERALPURPOSE, TEMPORARY);

    // TODO: special registers??

    private final String name;
    private final List<RegisterType> regTypes;

    private RiscvRegister(String name, RegisterType... registerTypes) {
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
