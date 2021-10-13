package org.specs.Arm.instruction;

import java.util.Arrays;

import org.specs.Arm.asm.ArmRegister;

import pt.up.fe.specs.binarytranslation.instruction.register.ARegisterDump;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;

public class ArmRegisterDump extends ARegisterDump {

    public ArmRegisterDump(String rawRegisterDump) {
        super(ARegisterDump.parseRegisters(Arrays.asList(ArmRegister.values()), rawRegisterDump));
    }

    /*
     * Private copy constructor
     */
    private ArmRegisterDump(ArmRegisterDump other) {
        super(other);
    }

    @Override
    public RegisterDump copy() {
        return new ArmRegisterDump(this);
    }
}