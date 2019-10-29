package org.specs.MicroBlaze.instruction;

import static pt.up.fe.specs.binarytranslation.instruction.OperandType.*;

import java.util.Arrays;
import java.util.List;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldData;

import pt.up.fe.specs.binarytranslation.instruction.OperandPropertiesData;
import pt.up.fe.specs.binarytranslation.instruction.OperandType;

/**
 * Enum is only a helper initializer to minimize verbosity in {@link MicroBlazeAsmFieldData}
 * 
 * @author nuno
 *
 */
public enum MicroBlazeOperandPropertiesData implements OperandPropertiesData {

    register_read("r", "", 32, REGISTER, READ),
    register_write("r", "", 32, REGISTER, WRITE),
    immediate("0x", "", 32, IMMEDIATE, READ);

    protected final String prefix;
    protected final String suffix;
    protected final int width;
    protected final List<OperandType> genericType;
    protected final OperandType maintype;

    private MicroBlazeOperandPropertiesData(String prefix, String suffix, int width, OperandType... type) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.width = width;
        this.genericType = Arrays.asList(type);
        this.maintype = this.genericType.get(0);
    }

    /*
     * Getters
     */
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
        return this.genericType.get(0);
    }
}
