package org.specs.MicroBlaze.isa;

import static pt.up.fe.specs.binarytranslation.OperandType.*;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.OperandProperties;
import pt.up.fe.specs.binarytranslation.OperandType;

public enum MicroBlazeOperandProperties implements OperandProperties {

    register("r", "", 32, REGISTER),
    immediate("0x", "", 32, IMMEDIATE),
    symbolic_register("r<", ">", 32, REGISTER, SYMBOLIC),
    symbolic_immediate("<", ">", 32, IMMEDIATE, SYMBOLIC);

    private final String prefix;
    private final String suffix;
    private final int width;
    private final List<OperandType> genericType;

    private MicroBlazeOperandProperties(String prefix, String suffix, int width, OperandType... type) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.width = width;
        this.genericType = Arrays.asList(type);
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public int getWidth() {
        return this.width;
    }

    public List<OperandType> getTypes() {
        return this.genericType;
    }
}
