package org.specs.Riscv.instruction;

import java.util.ArrayList;
import java.util.List;

import org.specs.Riscv.parsing.RiscvAsmFieldData;

import pt.up.fe.specs.binarytranslation.instruction.InstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class RiscvInstructionData extends InstructionData {

    /**
     * 
     */
    private static final long serialVersionUID = -2263355591235910478L;

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
    }

    /*
     * Helper constructor for copy, calls super copy
     */
    private RiscvInstructionData(String plainname,
            int latency, int delay, List<InstructionType> genericType,
            List<Operand> ops, Number branchTarget) {
        super(plainname, latency, delay, genericType, ops);
        this.branchTarget = branchTarget;
    }

    /*
     * Copy "constructor"
     */
    public RiscvInstructionData copy() {
        String copyname = new String(this.plainname);
        List<InstructionType> copytype = new ArrayList<InstructionType>(this.genericType);

        List<Operand> copyops = new ArrayList<Operand>();
        for (Operand op : this.operands)
            copyops.add(op.copy());

        return new RiscvInstructionData(copyname, this.latency,
                this.delay, copytype, copyops, this.branchTarget);
    }

    /*
    * Get target of branch if instruction is branch
    */
    public Number getBranchTarget() {
        return branchTarget;
    }
}
