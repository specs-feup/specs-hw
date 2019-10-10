package org.specs.MicroBlaze.isa;

import pt.up.fe.specs.binarytranslation.InstructionData;
import pt.up.fe.specs.binarytranslation.InstructionProperties;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;

public class MicroBlazeInstructionData extends InstructionData {

    public MicroBlazeInstructionData(InstructionProperties props, AsmInstructionData fieldData) {
        super(props, fieldData);
    }
}
