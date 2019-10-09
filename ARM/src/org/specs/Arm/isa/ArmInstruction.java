package org.specs.Arm.isa;

import java.util.Arrays;

import pt.up.fe.specs.binarytranslation.InstructionSet;
import pt.up.fe.specs.binarytranslation.generic.AInstruction;

public class ArmInstruction extends AInstruction {

    /*
     * Fields only relevant for ARM instructions
     */
    private ArmInstructionConditions condition;

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

        // this.condition = fieldData.get
    }

    @Override
    public Number getBranchTarget() {
        // TODO Auto-generated method stub
        return null;
    }
}
