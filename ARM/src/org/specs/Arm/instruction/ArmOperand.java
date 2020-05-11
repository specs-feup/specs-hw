package org.specs.Arm.instruction;

import static pt.up.fe.specs.binarytranslation.instruction.OperandType.*;

import org.specs.Arm.parsing.ArmAsmField;

import pt.up.fe.specs.binarytranslation.instruction.*;

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
        if (value.intValue() != SPvalue) {
            return new ArmOperand(props, value);

        } else {
            props.getTypes().add(SPECIAL);
            return new ArmOperand(props, "sp");
        }
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
        String prefix = (value instanceof Float) ? "" : "#0x";
        var props = new AOperandProperties(field, prefix, "", width, IMMEDIATE, READ);
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
     * Special PSTATE Register (can only be an output)
     * (see C6.1.4 to check which instructions write to the PSTATE register)
     */
    public static ArmOperand newPSTATERegister(OperandType rw) {
        var props = new AOperandProperties(ArmAsmField.IMPLICIT, "[", "]", 4, REGISTER, SPECIAL, rw);
        return new ArmOperand(props, "nzvc");
    }

    /*
     * Copy "constructor"
     */
    @Override
    public ArmOperand copy() {
        var props = this.getProperties().copy();
        if (this.isSubOperation() || this.isSpecial())
            return new ArmOperand(props, this.getStringValue());
        else
            return new ArmOperand(props, this.getValue());
    }
}
