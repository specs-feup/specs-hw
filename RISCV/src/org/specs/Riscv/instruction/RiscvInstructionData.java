package org.specs.Riscv.instruction;

import org.specs.Riscv.parsing.RiscvAsmFieldData;

import pt.up.fe.specs.binarytranslation.instruction.InstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;

public class RiscvInstructionData extends InstructionData {

    /*
     * Fields only relevant for RISC-V instructions
     */
    private final Number branchTarget;

    /*
     * Only public constructor
     */
    public RiscvInstructionData(InstructionProperties props, RiscvAsmFieldData fieldData) {
        super(props);
        this.operands = fieldData.getOperands();
        this.branchTarget = fieldData.getBranchTarget();
        // TODO: some of the code in getOperands and getBranchTarget is repeated
        // and could easily break :(
    }

    /*
     * Helper constructor for copy, calls super copy
     */
    private RiscvInstructionData(RiscvInstructionData other) {
        super(other);
        this.branchTarget = other.getBranchTarget();
    }

    /*
     * Copy "constructor"
     */
    public RiscvInstructionData copy() {
        return new RiscvInstructionData(this);
    }

    /*
    * Get target of branch if instruction is branch
    */
    public Number getBranchTarget() {
        return branchTarget;
    }
}
