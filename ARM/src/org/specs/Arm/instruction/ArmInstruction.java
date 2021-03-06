package org.specs.Arm.instruction;

import java.util.Arrays;

import org.specs.Arm.parsing.ArmAsmFieldData;
import org.specs.Arm.parsing.ArmAsmFieldType;
import org.specs.Arm.parsing.ArmIsaParser;

import pt.up.fe.specs.binarytranslation.instruction.AInstruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.instruction.InstructionSet;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;

/**
 * Implementation of AInstruction, specialized for ARMv8 project and ISA Relies on {@link ArmAsmFieldData} and
 * {@link ArmIsaParser} to initialize internal data.
 * 
 * @author NunoPaulino
 * 
 */
public class ArmInstruction extends AInstruction {

    // raw field data
    private final ArmAsmFieldData fieldData;

    // shared by all instructions, so they can go parse themselves
    private static ArmIsaParser parser;

    /*
     * Binary Parser and "isa decoder" Shared by all
     */
    static {
        parser = new ArmIsaParser();
        instSet = new InstructionSet(Arrays.asList(ArmInstructionProperties.values()),
                Arrays.asList(ArmAsmFieldType.values()));
    }

    /*
     * Static "constructor"
     */
    public static ArmInstruction newInstance(String address, String instruction) {
        var fieldData = parser.parse(address, instruction);
        var props = instSet.process(fieldData);
        var idata = new ArmInstructionData(props, fieldData);
        return new ArmInstruction(address, instruction, idata, fieldData, props);
    }

    /*
     * Create the instruction
     */
    private ArmInstruction(String address, String instruction, InstructionData idata,
            ArmAsmFieldData fieldData, InstructionProperties props) {
        super(Long.parseLong(address, 16), instruction, idata, props);
        this.fieldData = fieldData;
    }

    /*
     * Copy "constructor"
     */
    @Override
    public ArmInstruction copy() {

        String copyaddr = new String(Long.toHexString(this.getAddress().intValue()));
        String copyinst = new String(this.getInstruction());
        ArmInstructionData copyData = this.getData().copy();
        ArmAsmFieldData copyFieldData = this.getFieldData().copy();
        return new ArmInstruction(copyaddr, copyinst, copyData, copyFieldData, this.getProperties());
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
            long branchTarget = (this.getData().getBranchTarget()).longValue();

            if (this.getData().getGenericTypes().contains(InstructionType.G_RJUMP)) {
                var v = this.getAddress().longValue();
                return branchTarget = v + branchTarget;
            }

            else if (this.getData().getGenericTypes().contains(InstructionType.G_AJUMP))
                return branchTarget;
        }
        return null; // if not a jump, just jumps to next instruction
        // TODO either replace this with empty, or replace with getInstructionWidth
    }
    // TODO promote this into AInstruction??

    @Override
    public ArmAsmFieldData getFieldData() {
        return this.fieldData;
    }

    @Override
    public InstructionPseudocode getPseudocode() {

        var values = ArmPseudocode.values();
        for (var e : values) {
            if (e.getName().equals(this.getData().getPlainName()))
                return e;
        }

        /*var pseudocode = Enums.getIfPresent(ArmPseudocode.class, this.getData().getPlainName());
        if (pseudocode.isPresent())
            return pseudocode.get();
        else*/
        return ArmPseudocode.getDefault();
    }
}
