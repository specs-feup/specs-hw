package org.specs.MicroBlaze.v2;

import pt.up.fe.specs.binarytranslation.generic.GenericInstruction;

public class MicroBlazeInstruction extends GenericInstruction {

    /*
     * 
     */
    public MicroBlazeInstruction(Number address, String instruction) {
        super(address, instruction);
        long fullopcode = Long.parseLong(instruction, 16);
        this.latency = MicroBlazeInstructionSetFields.getLatency(fullopcode);
        this.genericType = MicroBlazeInstructionSetFields.getGenericType(fullopcode);
        // lookup ISA table for static information
    }

    @Override
    public String getInstruction() {
        return null;
    }

    @Override
    public Number getBranchTarget() {
        if (this.isJump()) {
            int fullopcode = Integer.valueOf(this.getInstruction());
            return (fullopcode & 0x0000FFFF);
            // TODO replace mask with mask built based on elf instruction width
            // or info about instruction set
        }

        return null;
    }
}
