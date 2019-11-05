package org.specs.Arm.instruction;

import static pt.up.fe.specs.binarytranslation.instruction.OperandType.*;

import org.specs.Arm.parsing.ArmAsmField;

import pt.up.fe.specs.binarytranslation.instruction.AOperand;
import pt.up.fe.specs.binarytranslation.instruction.AOperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.OperandProperties;

public class ArmOperand extends AOperand {

    static int SPvalue = 31;

    /*
     * Integer value constructor
     */
    private ArmOperand(OperandProperties props, Number value) {
        super(props, value);
    }

    /*
     * String value constructor
     */
    private ArmOperand(OperandProperties props, String value) {
        super(props, value);
    }

    private static ArmOperand newInstance(OperandProperties props, Number value) {
        if (value.intValue() != SPvalue)
            return new ArmOperand(props, value);
        else
            return new ArmOperand(props, "sp");

    }

    private static String getPrefix(int width) {
        switch (width) {
        case 32:
            return "w";
        case 64:
            return "x";
        default:
            return "x";
        }
    }

    private static String getSIMDPrefix(int width) {
        switch (width) {
        case 8:
            return "b";
        case 16:
            return "h";
        case 32:
            return "s";
        case 64:
            return "d";
        case 128:
            return "q";
        default:
            return "s";
        }
    }

    /*
     * Read register
     */
    public static ArmOperand newReadRegister(ArmAsmField field, Number value, int width) {
        String prefix = (value.intValue() == SPvalue) ? "" : getPrefix(width);
        var props = new AOperandProperties(field, prefix, "", width, REGISTER, READ);
        return newInstance(props, value);
    }

    /*
     * Write register
     */
    public static ArmOperand newWriteRegister(ArmAsmField field, Number value, int width) {
        String prefix = (value.intValue() == SPvalue) ? "" : getPrefix(width);
        var props = new AOperandProperties(field, prefix, "", width, REGISTER, WRITE);
        return newInstance(props, value);
    }

    /*
     * Read simd register
     */
    public static ArmOperand newSIMDReadRegister(ArmAsmField field, Number value, int width) {
        String prefix = getSIMDPrefix(width);
        var props = new AOperandProperties(field, prefix, "", width, REGISTER, READ, SIMD);
        return newInstance(props, value);
    }

    /*
     * Write simd register
     */
    public static ArmOperand newSIMDWriteRegister(ArmAsmField field, Number value, int width) {
        String prefix = getSIMDPrefix(width);
        var props = new AOperandProperties(field, prefix, "", width, REGISTER, WRITE, SIMD);
        return newInstance(props, value);
    }

    /*
     * Immediate
     */
    public static ArmOperand newImmediate(ArmAsmField field, Number value, int width) {
        var props = new AOperandProperties(field, "#0x", "", width, IMMEDIATE, READ);
        return newInstance(props, value);
    }

    /*
     * Immediate without prefix (used for labels?)
     */
    public static ArmOperand newImmediateLabel(ArmAsmField field, Number value, int width) {
        var props = new AOperandProperties(field, "", "", width, IMMEDIATE, READ);
        return newInstance(props, value);
    }

    /*
     * Representational sub-operation operand for shifted (and extended?) register operations
     */
    public static ArmOperand newSubOperation(ArmAsmField field, String value, int width) {
        var props = new AOperandProperties(field, "", "", width, SUBOPERATION);
        return new ArmOperand(props, value);
    }

    /*
     * Copy "constructor"
     */
    @Override
    public ArmOperand copy() {
        var props = this.getProperties().copy();
        return new ArmOperand(props, this.getIntegerValue().intValue());
    }
}
