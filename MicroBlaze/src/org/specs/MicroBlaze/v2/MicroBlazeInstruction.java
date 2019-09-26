package org.specs.MicroBlaze.v2;

import pt.up.fe.specs.binarytranslation.generic.GenericInstruction;

public class MicroBlazeInstruction extends GenericInstruction {

    /*
     * 
     */
    public MicroBlazeInstruction(Number address, String instruction) {
        super(address, instruction);
        long fullopcode = Long.parseLong(instruction, 16);
        this.plainname = MicroBlazeInstructionSetFields.getName(fullopcode);
        this.delay = MicroBlazeInstructionSetFields.getDelay(fullopcode);
        this.latency = MicroBlazeInstructionSetFields.getLatency(fullopcode);
        this.genericType = MicroBlazeInstructionSetFields.getGenericType(fullopcode);
        // lookup ISA table for static information
    }

    @Override
    public Number getBranchTarget() {
        if (this.isJump()) {
            long fullopcode = Long.parseLong(this.getInstruction(), 16);
            short jmpval = (short) (fullopcode & 0x0000FFFF);
            return (this.getAddress().longValue() + (long) jmpval);
            // TODO replace mask with mask built based on elf instruction width
            // or info about instruction set
        }

        return null;
    }
}
