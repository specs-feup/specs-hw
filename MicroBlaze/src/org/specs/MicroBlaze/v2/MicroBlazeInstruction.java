package org.specs.MicroBlaze.v2;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.InstructionType;

public class MicroBlazeInstruction implements Instruction {

    private Number address;
    private int latency;
    private String instruction;
    private InstructionType genericType;

    /*
     * 
     */
    public MicroBlazeInstruction(Number address, String instruction) {
        this.address = address;
        this.instruction = instruction;
        // addr:inst pair as it appears in ELF or trace (i.e. with arguments)

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

    @Override
    public boolean isAdd() {
        return this.genericType == InstructionType.add;
    }

    @Override
    public boolean isSub() {
        return this.genericType == InstructionType.sub;
    }

    @Override
    public boolean isLogical() {
        return this.genericType == InstructionType.logical;
    }

    @Override
    public boolean isUnaryLogical() {
        return this.genericType == InstructionType.unarylogical;
    }

    @Override
    public boolean isConditionalJump() {
        return this.genericType == InstructionType.cjump;
    }

    @Override
    public boolean isUnconditionalJump() {
        return this.genericType == InstructionType.ujump;
    }

    @Override
    public boolean isStore() {
        return this.genericType == InstructionType.store;
    }

    @Override
    public boolean isLoad() {
        return this.genericType == InstructionType.load;
    }
}
