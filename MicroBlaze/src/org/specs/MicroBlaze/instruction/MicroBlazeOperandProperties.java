package org.specs.MicroBlaze.instruction;

import static pt.up.fe.specs.binarytranslation.instruction.OperandType.*;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.OperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.OperandType;

public enum MicroBlazeOperandProperties implements OperandProperties {

    register_read("r", "", 32, REGISTER, READ),
    register_write("r", "", 32, REGISTER, WRITE),

    symbolic_register_read("r<", ">", 32, REGISTER, SYMBOLIC, READ),
    symbolic_register_write("r<", ">", 32, REGISTER, SYMBOLIC, WRITE),

    immediate("0x", "", 32, IMMEDIATE),
    symbolic_immediate("imm<", ">", 32, IMMEDIATE, SYMBOLIC);

    private final String prefix;
    private final String suffix;
    private final int width;
    private final List<OperandType> genericType;
    private final OperandType maintype;

    private MicroBlazeOperandProperties(String prefix, String suffix, int width, OperandType... type) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.width = width;
        this.genericType = Arrays.asList(type);
        this.maintype = this.genericType.get(0);
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

    public OperandType getMainType() {
        return this.maintype;
    }

    public String getName() {
        return this.name();
    }
}
