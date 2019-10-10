package org.specs.Arm.isa;

import java.util.Arrays;

import pt.up.fe.specs.binarytranslation.InstructionSet;
import pt.up.fe.specs.binarytranslation.generic.AInstruction;

public class ArmInstruction extends AInstruction {

    /*
     * Binary Parser and "isa decoder" Shared by all
     */
    static {
        parser = ArmInstructionParsers.getArmIsaParser();
        instSet = new InstructionSet(Arrays.asList(ArmInstructionProperties.values()),
                Arrays.asList(ArmInstructionType.values()));
    }

    /*
     * Create the instruction
     */
    public ArmInstruction(String address, String instruction) {
        super(Long.parseLong(address, 16), instruction);

        ArmAsmInstructionData armidata = new ArmAsmInstructionData(this.fieldData);
        this.fieldData = armidata;
        // build child from parent

        this.idata = new ArmInstructionData(instSet.process(fieldData), armidata);
    }

    @Override
    public int getBitWidth() {
        return this.idata.getBitwidth();
    }

    @Override
    public Boolean isSimd() {
        return this.idata.getIsSimd();
    }

    @Override
    public Number getBranchTarget() {
        // TODO Auto-generated method stub
        return null;
    }
}
