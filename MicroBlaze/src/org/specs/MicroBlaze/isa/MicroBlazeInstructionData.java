package org.specs.MicroBlaze.isa;

import pt.up.fe.specs.binarytranslation.InstructionData;
import pt.up.fe.specs.binarytranslation.InstructionProperties;

public class MicroBlazeInstructionData extends InstructionData {

    public MicroBlazeInstructionData(InstructionProperties props, MicroBlazeAsmInstructionData fieldData) {
        super(props);
        this.operands = fieldData.getOperands();
    }
}
