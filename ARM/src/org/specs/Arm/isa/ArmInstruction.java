package org.specs.Arm.isa;

import java.util.Arrays;

import pt.up.fe.specs.binarytranslation.InstructionData;
import pt.up.fe.specs.binarytranslation.InstructionSet;
import pt.up.fe.specs.binarytranslation.generic.AInstruction;

/**
 * Implementation of AInstruction, specialized for ARMv8 project and ISA Relies on {@link ArmAsmInstructionData} and
 * {@link ArmIsaParser} to initialize internal data.
 * 
 * @author NunoPaulino
 * 
 */
public class ArmInstruction extends AInstruction {

    // raw field data
    private final ArmAsmInstructionData fieldData;

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

    public static ArmInstruction newInstance(String address, String instruction) {
        var fieldData = parser.parse(instruction);
        var idata = new ArmInstructionData(instSet.process(fieldData), fieldData);
        return new ArmInstruction(address, instruction, idata, fieldData);
    }

    /*
     * Create the instruction
     */
    private ArmInstruction(String address, String instruction, InstructionData idata, ArmAsmInstructionData fieldData) {
        super(Long.parseLong(address, 16), instruction, idata);
        this.fieldData = fieldData;
    }

    @Override
    public ArmInstructionData getData() {
        // idata is guaranteed to be an (ArmInstructionData)
        return (ArmInstructionData) super.getData();
    }

    @Override
    public Number getBranchTarget() {
        // TODO Auto-generated method stub
        return null;
    }

    public ArmAsmInstructionData getFieldData() {
        return this.fieldData;
    }

}
