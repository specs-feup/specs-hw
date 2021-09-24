package org.specs.Riscv.instruction;

import java.util.Arrays;

import org.specs.Riscv.asm.RiscvRegister;

import pt.up.fe.specs.binarytranslation.instruction.register.ARegisterDump;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;

public class RiscvRegisterDump extends ARegisterDump {

    public RiscvRegisterDump(String rawRegisterDump) {
        super(ARegisterDump.parseRegisters(Arrays.asList(RiscvRegister.values()), rawRegisterDump));
    }

    /*
     * Private copy constructor
     */
    private RiscvRegisterDump(RiscvRegisterDump other) {
        super(other);
    }

    @Override
    public RegisterDump copy() {
        return new RiscvRegisterDump(this);
    }
}
