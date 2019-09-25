package org.specs.MicroBlaze.v2;

import pt.up.fe.specs.binarytranslation.generic.GenericInstruction;

public class MicroBlazeInstruction extends GenericInstruction {

    /*
     * 
     */
    public MicroBlazeInstruction(Number address, String instruction) {
        super(address, instruction);
        int fullopcode = Integer.valueOf(instruction);
        this.latency = MicroBlazeInstructionSetFields.getLatency(fullopcode);
        this.genericType = MicroBlazeInstructionSetFields.getGenericType(fullopcode);
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
}
