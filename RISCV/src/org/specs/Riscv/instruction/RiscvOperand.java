package org.specs.Riscv.instruction;

import org.specs.Riscv.parsing.RiscvAsmField;

import pt.up.fe.specs.binarytranslation.instruction.operand.AOperand;
import pt.up.fe.specs.binarytranslation.instruction.operand.AOperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandAccessType;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandDataSize;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandDataType;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandType;

public class RiscvOperand extends AOperand {

    private RiscvOperand(OperandProperties props, int value) {
        super(props, value);
    }

    public static RiscvOperand newReadRegister(RiscvAsmField field, int value) {
        var props = new AOperandProperties(field, "a", "",
                OperandType.REGISTER, OperandAccessType.READ,
                OperandDataType.SCALAR_INTEGER, OperandDataSize.WORD);
        return new RiscvOperand(props, value);
    }

    public static RiscvOperand newWriteRegister(RiscvAsmField field, int value) {
        var props = new AOperandProperties(field, "a", "",
                OperandType.REGISTER, OperandAccessType.WRITE,
                OperandDataType.SCALAR_INTEGER, OperandDataSize.WORD);
        return new RiscvOperand(props, value);
    }

    public static RiscvOperand newImmediate(RiscvAsmField field, int value) {
        var props = new AOperandProperties(field, "0x", "",
                OperandType.IMMEDIATE, OperandAccessType.WRITE,
                OperandDataType.SCALAR_INTEGER, OperandDataSize.WORD);
        return new RiscvOperand(props, value);
    }

    /*
     * Copy "constructor"
     */
    @Override
    public RiscvOperand copy() {
        var props = this.getProperties().copy();
        return new RiscvOperand(props, this.getNumberValue().intValue());
    }
}
