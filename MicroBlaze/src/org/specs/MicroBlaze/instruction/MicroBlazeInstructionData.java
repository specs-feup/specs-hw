package org.specs.MicroBlaze.instruction;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldData;

import pt.up.fe.specs.binarytranslation.instruction.InstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;

public class MicroBlazeInstructionData extends InstructionData {

    /*
     * Only public constructor
     */
    public MicroBlazeInstructionData(
            InstructionProperties props,
            MicroBlazeAsmFieldData fieldData,
            MicroBlazeRegisterDump registers) {
        super(props, fieldData, registers);
    }

    /*
     * Helper constructor for copy, calls super copy
     */
    private MicroBlazeInstructionData(MicroBlazeInstructionData other) {
        super(other);
    }

    /*
     * Copy "constructor"
     */
    @Override
    public MicroBlazeInstructionData copy() {
        return new MicroBlazeInstructionData(this);
    }
}
