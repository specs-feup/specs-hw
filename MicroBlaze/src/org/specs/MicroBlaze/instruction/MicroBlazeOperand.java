package org.specs.MicroBlaze.instruction;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmField;

import pt.up.fe.specs.binarytranslation.instruction.operand.AOperand;
import pt.up.fe.specs.binarytranslation.instruction.operand.AOperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandAccessType;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandDataSize;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandDataType;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandType;

public class MicroBlazeOperand extends AOperand {

    private MicroBlazeOperand(OperandProperties props, int value, Number dataValue) {
        super(props, value, dataValue);
    }

    /*
     * Copy constructor
     */
    private MicroBlazeOperand(MicroBlazeOperand other) {
        super(other);
    }

    /*
     * public copy method
     */
    @Override
    public MicroBlazeOperand copy() {
        return new MicroBlazeOperand(this);
    }

    public static MicroBlazeOperand newReadRegister(MicroBlazeAsmField field, int value, Number dataValue) {
        var props = new AOperandProperties(field, "r", "",
                OperandType.REGISTER, OperandAccessType.READ,
                OperandDataType.SCALAR_INTEGER, OperandDataSize.WORD);
        return new MicroBlazeOperand(props, value, dataValue);
    }

    public static MicroBlazeOperand newWriteRegister(MicroBlazeAsmField field, int value, Number dataValue) {
        var props = new AOperandProperties(field, "r", "",
                OperandType.REGISTER, OperandAccessType.WRITE,
                OperandDataType.SCALAR_INTEGER, OperandDataSize.WORD);
        return new MicroBlazeOperand(props, value, dataValue);
    }

    public static MicroBlazeOperand newImmediate(MicroBlazeAsmField field, int value, Number dataValue) {
        var props = new AOperandProperties(field, "0x", "",
                OperandType.IMMEDIATE, OperandAccessType.READ,
                OperandDataType.SCALAR_INTEGER, OperandDataSize.WORD);
        return new MicroBlazeOperand(props, value, dataValue);
    }
}
