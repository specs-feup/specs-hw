package org.specs.MicroBlaze.isa;

import pt.up.fe.specs.binarytranslation.InstructionOperandType;
import pt.up.fe.specs.binarytranslation.generic.AInstructionOperand;

public class MicroBlazeInstructionOperand extends AInstructionOperand {

    public MicroBlazeInstructionOperand(InstructionOperandType type, Integer value) {
        super(type, value);
    }

    @Override
    public String getRepresentation() {
        String ret = "";
        if (this.type == MicroBlazeInstructionOperandType.register)
            ret = (type.getPrefix() + Integer.toString(this.value));

        else if (type == MicroBlazeInstructionOperandType.immediate)
            ret = (type.getPrefix() + "0x" + Integer.toHexString(this.value));

        return ret;
    }
}
