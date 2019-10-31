package org.specs.Arm.instruction;

import static pt.up.fe.specs.binarytranslation.instruction.OperandType.*;

import org.specs.Arm.parsing.ArmAsmField;

import pt.up.fe.specs.binarytranslation.instruction.AOperand;
import pt.up.fe.specs.binarytranslation.instruction.AOperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.OperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.OperandType;

public class ArmOperand extends AOperand {

    /*
     * 
     */
    private ArmOperand(OperandProperties props, Integer value) {
        super(props, value);
    }

    private static OperandType resolveWidth(int width) {
        switch (width) {
        case 4:
            return NIBBLE;
        case 8:
            return BYTE;
        case 16:
            return HALFWORD;
        case 32:
            return WORD;
        case 64:
            return DWORD;
        case 128:
            return QWORD;
        default:
            return WORD;
        }
    }

    private static String getPrefix(int width) {
        switch (width) {
        case 32:
            return "W";
        case 64:
            return "X";
        default:
            return "X";
        }
    }

    /*
     * Read register
     */
    public static ArmOperand newReadRegister(ArmAsmField field, Integer value, int width) {
        OperandType wd = resolveWidth(width);
        var props = new AOperandProperties(field, getPrefix(width), "", width, REGISTER, READ, wd);
        return new ArmOperand(props, value);
    }

    /*
     * Write register
     */
    public static ArmOperand newWriteRegister(ArmAsmField field, Integer value, int width) {
        OperandType wd = resolveWidth(width);
        var props = new AOperandProperties(field, getPrefix(width), "", width, REGISTER, WRITE, wd);
        return new ArmOperand(props, value);
    }

    /*
     * Immediate
     */
    public static ArmOperand newImmediate(ArmAsmField field, Integer value, int width) {
        OperandType wd = resolveWidth(width);
        var props = new AOperandProperties(field, "", "", width, IMMEDIATE, READ, wd);
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
