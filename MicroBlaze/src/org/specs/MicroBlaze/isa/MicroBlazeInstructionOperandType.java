package org.specs.MicroBlaze.isa;

import pt.up.fe.specs.binarytranslation.InstructionOperandType;

public enum MicroBlazeInstructionOperandType implements InstructionOperandType {

    register("r", 32),
    immediate("", 32);

    private String prefix;
    private int width;

    private MicroBlazeInstructionOperandType(String prefix, int width) {
        this.prefix = prefix;
        this.width = width;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public Boolean isRegister() {
        return (this.name() == "register");
    }
}
