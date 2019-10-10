package org.specs.Arm.isa;

import pt.up.fe.specs.binarytranslation.InstructionData;
import pt.up.fe.specs.binarytranslation.InstructionProperties;

public class ArmInstructionData extends InstructionData {

    /*
     * Fields only relevant for ARM instructions
     */
    private int bitwidth;
    private Boolean isSimd;
    private ArmInstructionCondition condition;

    public ArmInstructionData(InstructionProperties props, ArmAsmInstructionData fieldData) {
        super(props, fieldData);
        this.bitwidth = fieldData.getBitWidth();
        this.isSimd = fieldData.isSimd();
        this.condition = fieldData.getCond();
    }

    public int getBitwidth() {
        return bitwidth;
    }

    public Boolean getIsSimd() {
        return isSimd;
    }
}
