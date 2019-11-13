package org.specs.Arm.instruction;

import org.specs.Arm.parsing.ArmAsmFieldData;

import pt.up.fe.specs.binarytranslation.instruction.InstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;

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
