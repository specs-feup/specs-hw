package org.specs.Arm.isa;

import java.util.Arrays;

import pt.up.fe.specs.binarytranslation.InstructionData;
import pt.up.fe.specs.binarytranslation.InstructionSet;
import pt.up.fe.specs.binarytranslation.InstructionType;
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

        var v = ArmInstructionProperties.values();
        var b = ArmInstructionType.values();

        instSet = new InstructionSet(Arrays.asList(ArmInstructionProperties.values()),
                Arrays.asList(ArmInstructionType.values()));
    }

    /*
     * Static "constructor"
     */
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
    /*
     * target addr of branch is current addr of this inst + branchTarget (if relative jump)
     * or branchTarget (is absolute jump)
     */
    public Number getBranchTarget() {
        if (this.isJump()) {
            int branchTarget = this.getData().getBranchTarget();

            if (this.getData().getGenericTypes().contains(InstructionType.G_RJUMP)) {
                var v = this.getAddress().intValue();
                return branchTarget = v + branchTarget;
            }

            else if (this.getData().getGenericTypes().contains(InstructionType.G_AJUMP))
                return branchTarget;
        }
        return 4; // if not a jump, just jumps to next instruction
        // TODO either replace this with empty, or replace with getInstructionWidth
    }
    // TODO promote this into AInstruction??

    public ArmAsmInstructionData getFieldData() {
        return this.fieldData;
    }

}
