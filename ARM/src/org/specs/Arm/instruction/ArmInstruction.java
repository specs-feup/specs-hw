package org.specs.Arm.instruction;

import java.util.Arrays;

import org.specs.Arm.parsing.ArmAsmFieldData;
import org.specs.Arm.parsing.ArmAsmFieldType;
import org.specs.Arm.parsing.ArmIsaParser;

import pt.up.fe.specs.binarytranslation.instruction.AInstruction;
import pt.up.fe.specs.binarytranslation.instruction.AInstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.instruction.InstructionSet;

/**
 * Implementation of AInstruction, specialized for ARMv8 project and ISA Relies on {@link ArmAsmFieldData} and
 * {@link ArmIsaParser} to initialize internal data.
 * 
 * @author NunoPaulino
 * 
 */
public class ArmInstruction extends AInstruction {

    /*
     * Parser and "decoder" Shared by all
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
        return ArmInstruction.newInstance(address, instruction, null);
    }

    /*
     * Static "constructor"
     */
    public static ArmInstruction newInstance(String address, String instruction, String rawRegisterDump) {
        var fieldData = (ArmAsmFieldData) parser.parse(address, instruction);
        var props = instSet.process(fieldData);
        var idata = new ArmInstructionData(props, fieldData, new ArmRegisterDump(rawRegisterDump));
        var inst = new ArmInstruction(address, instruction, idata);
        return inst;
    }

    /*
     * Create the instruction
     */
    private ArmInstruction(String address, String instruction, AInstructionData idata) {
        super(Long.parseLong(address, 16), instruction, idata);
    }

    /*
     * Helper constructor for copy, calls super copy
     */
    private ArmInstruction(ArmInstruction other) {
        super(other);
    }

    /*
     * Copy "constructor"
     */
    @Override
    public ArmInstruction copy() {
        return new ArmInstruction(this);

        /*
        String copyaddr = new String(Long.toHexString(this.getAddress().intValue()));
        String copyinst = new String(this.getInstruction());
        ArmInstructionData copyData = this.getData().copy();
        ArmAsmFieldData copyFieldData = this.getFieldData().copy();
        return new ArmInstruction(copyaddr, copyinst, copyData, copyFieldData, this.getProperties());*/
    }

    @Override
    public ArmInstructionData getData() {
        // idata is guaranteed to be an (ArmInstructionData)
        return (ArmInstructionData) super.getData();
    }

    /*@Override
    
     * target addr of branch is current addr of this inst + branchTarget (if relative jump)
     * or branchTarget (is absolute jump)
     
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
    }*/

    @Override
    public String getName() {
        return this.getData().getAdjustedName();
    }

    @Override
    public InstructionPseudocode getPseudocode() {

        // need to look for subfield in enum
        // (and not enum name) due to adjusting for conditionals
        for (var e : ArmPseudocode.values()) {
            if (e.getName().equals(this.getName()))
                return e;
        }
        return ArmPseudocode.getDefault();
    }
}
