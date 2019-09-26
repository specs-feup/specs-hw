package org.specs.Arm.isa;

import pt.up.fe.specs.binarytranslation.generic.GenericInstruction;

public class ArmInstruction extends GenericInstruction {

    /*
    * 
    */
    public ArmInstruction(Number address, String instruction) {
        super(address, instruction);
        long fullopcode = Integer.valueOf(instruction);
        this.latency = ArmInstructionSetFields.getLatency(fullopcode);
        this.genericType = ArmInstructionSetFields.getGenericType(fullopcode);
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
