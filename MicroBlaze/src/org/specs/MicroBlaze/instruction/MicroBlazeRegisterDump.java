package org.specs.MicroBlaze.instruction;

import java.util.Arrays;

import org.specs.MicroBlaze.asm.MicroBlazeRegister;

import pt.up.fe.specs.binarytranslation.instruction.register.ARegisterDump;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;

public class MicroBlazeRegisterDump extends ARegisterDump {

    public MicroBlazeRegisterDump(String rawRegisterDump) {
        super(ARegisterDump.parseRegisters(Arrays.asList(MicroBlazeRegister.values()), rawRegisterDump));
    }

    /*
     * Private copy constructor
     */
    private MicroBlazeRegisterDump(MicroBlazeRegisterDump other) {
        super(other);
    }

    @Override
    public RegisterDump copy() {
        return new MicroBlazeRegisterDump(this);
    }
}
