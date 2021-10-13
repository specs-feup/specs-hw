package org.specs.Arm.asm;

import static pt.up.fe.specs.binarytranslation.instruction.register.RegisterType.HARDZERO;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.register.Register;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterType;

public enum ArmRegister implements Register {

    // GP bank
    R0("r0", HARDZERO);

    // TODO: all others!!

    private final String name;
    private final List<RegisterType> regTypes;

    private ArmRegister(String name, RegisterType... registerTypes) {
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
