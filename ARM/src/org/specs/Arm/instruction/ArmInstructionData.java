package org.specs.Arm.instruction;

import java.util.ArrayList;
import java.util.List;

import org.specs.Arm.parsing.ArmAsmFieldData;

import pt.up.fe.specs.binarytranslation.instruction.*;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

/**
 * This class contains the interpreted fields, accessible trough getters, which are read from the raw parser field data,
 * in "fieldData"; this class should not do any complex processing, and should be used only for data lookup
 * 
 * @author nuno
 *
 */
public class ArmInstructionData extends InstructionData {

    /*
     * Fields only relevant for ARM instructions
     */
    private final int bitwidth;
    private final Boolean isSimd;
    private final ArmInstructionCondition condition;
    private final Number branchTarget;

    public ArmInstructionData(InstructionProperties props, ArmAsmFieldData fieldData) {
        super(props);
        this.bitwidth = fieldData.getBitWidth();
        this.isSimd = fieldData.isSimd();
        this.condition = fieldData.getCond();
        this.operands = fieldData.getOperands();
        this.branchTarget = fieldData.getBranchTarget();
    }

    /*
     * Helper constructor for copy, calls super copy
     */
    private ArmInstructionData(String plainname,
            int latency, int delay, List<InstructionType> genericType, List<Operand> ops, Boolean isSimd,
            ArmInstructionCondition condition, Number branchTarget, int bitwidth) {
        super(plainname, latency, delay, genericType, ops);
        this.isSimd = isSimd;
        this.condition = condition;
        this.branchTarget = branchTarget;
        this.bitwidth = bitwidth;
    }

    /*
     * Copy "constructor"
     */
    public ArmInstructionData copy() {
        String copyname = new String(this.plainname);
        List<InstructionType> copytype = new ArrayList<InstructionType>(this.genericType);

        List<Operand> copyops = new ArrayList<Operand>();
        for (Operand op : this.operands)
            copyops.add(op.copy());

        return new ArmInstructionData(copyname, this.latency, this.delay, copytype, copyops, this.isSimd,
                this.condition, this.branchTarget, this.bitwidth);
    }

    /*
     * Some ARM instructions need a suffix (i.e., the conditionals)
     */
    @Override
    public String getPlainName() {
        if (this.condition.equals(ArmInstructionCondition.NONE)) {
            return this.plainname;
        } else {
            return this.plainname + "." + this.condition.getShorthandle();
        }
    }

    @Override
    public int getBitWidth() {
        return bitwidth;
    }

    @Override
    public Boolean isSimd() {
        return isSimd;
    }

    public Number getBranchTarget() {
        return branchTarget;
    }
}
