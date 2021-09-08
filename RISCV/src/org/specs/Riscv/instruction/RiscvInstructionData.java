package org.specs.Riscv.instruction;

import org.specs.Riscv.parsing.RiscvAsmFieldData;

import pt.up.fe.specs.binarytranslation.instruction.InstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;

public class RiscvInstructionData extends InstructionData {

    /*
     * Only public constructor
     */
    public RiscvInstructionData(InstructionProperties props, RiscvAsmFieldData fieldData) {
        super(props, fieldData);
        // TODO: some of the code in getOperands and getBranchTarget for RISCV is repeated
        // and could easily break :(
    }

    /*
     * Helper constructor for copy, calls super copy
     */
    private RiscvInstructionData(RiscvInstructionData other) {
        super(other);
    }

    /*
     * Copy "constructor"
     */
    @Override
    public RiscvInstructionData copy() {
        return new RiscvInstructionData(this);
    }
}
