package org.specs.Arm.isa;

import java.util.Arrays;

import pt.up.fe.specs.binarytranslation.InstructionSet;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.generic.AInstruction;

public class ArmInstruction extends AInstruction {

    private ArmAsmInstructionData fieldData; // raw field data

    // shared by all instructions, so they can go parse themselves
    private static ArmIsaParser parser;

    /*
     * Binary Parser and "isa decoder" Shared by all
     */
    static {
        parser = new ArmIsaParser();
        instSet = new InstructionSet(Arrays.asList(ArmInstructionProperties.values()),
                Arrays.asList(ArmInstructionType.values()));
    }

    /*
     * Create the instruction
     */
    public ArmInstruction(String address, String instruction) {
        super(Long.parseLong(address, 16), instruction);
        this.fieldData = parser.parse(instruction);
        this.idata = new ArmInstructionData(instSet.process(fieldData), this.fieldData);
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

    public AsmInstructionData getFields() {
        return this.fieldData;
    }
}
