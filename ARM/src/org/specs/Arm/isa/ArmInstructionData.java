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

    /*
     * Some ARM instructions need a suffix (i.e., the conditionals)
     */
    @Override
    public String getPlainName() {
        if (this.condition.equals(ArmInstructionCondition.NONE)) {
            return this.plainname;
        } else {
            return this.plainname + "." + this.condition.getShorthandle().toLowerCase();
        }
    }

    public int getBitWidth() {
        return bitwidth;
    }

    public Boolean isSimd() {
        return isSimd;
    }
}
