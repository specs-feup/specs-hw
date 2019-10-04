package org.specs.Arm.isa;

import java.util.Arrays;

import pt.up.fe.specs.binarytranslation.InstructionSet;
import pt.up.fe.specs.binarytranslation.generic.AInstruction;

public class ArmInstruction extends AInstruction {

    /*
     * Parser and "decoder" Shared by all
     */
    static {
        instSet = new InstructionSet(Arrays.asList(ArmInstructionProperties.values()),
                Arrays.asList(ArmInstructionType.values()));
        parser = ArmInstructionParsers.getArmIsaParser();
    }

    /*
     * Create the instruction
     */
    public ArmInstruction(String address, String instruction) {
        super(Long.parseLong(address, 16), instruction);
        this.fieldData = parser.parse(instruction);
        this.idata = instSet.process(fieldData);
        // lookup ISA table for static information
    }

    @Override
    public Number getAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getInstruction() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Number getBranchTarget() {
        // TODO Auto-generated method stub
        return null;
    }
}
