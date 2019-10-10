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
                Arrays.asList(ArmInstructionType.values()), ArmInstructionData::new);
    }

    /*
     * Create the instruction
     */
    public ArmInstruction(String address, String instruction) {
        super(Long.parseLong(address, 16), instruction);
    }

    @Override
    public int getBitWidth() {
        return 0;
    }

    @Override
    public Boolean isSimd() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Number getBranchTarget() {
        // TODO Auto-generated method stub
        return null;
    }
}
