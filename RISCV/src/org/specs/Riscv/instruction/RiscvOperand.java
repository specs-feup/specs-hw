package org.specs.Riscv.instruction;

import static pt.up.fe.specs.binarytranslation.instruction.operand.OperandType.*;

import org.specs.Riscv.parsing.RiscvAsmField;

import pt.up.fe.specs.binarytranslation.instruction.operand.AOperand;
import pt.up.fe.specs.binarytranslation.instruction.operand.AOperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandProperties;

public class RiscvOperand extends AOperand {

    /**
     * 
     */
    private static final long serialVersionUID = -1794402786519400724L;

    private RiscvOperand(OperandProperties props, int value) {
        super(props, value);
    }

    public static RiscvOperand newReadRegister(RiscvAsmField field, int value) {
        var props = new AOperandProperties(field, "a", "", 32, REGISTER, READ, WORD);
        return new RiscvOperand(props, value);
    }

    public static RiscvOperand newWriteRegister(RiscvAsmField field, int value) {
        var props = new AOperandProperties(field, "a", "", 32, REGISTER, WRITE, WORD);
        return new RiscvOperand(props, value);
    }

    public static RiscvOperand newImmediate(RiscvAsmField field, int value) {
        var props = new AOperandProperties(field, "0x", "", 32, IMMEDIATE, READ, WORD);
        return new RiscvOperand(props, value);
    }

    /*
     * Copy "constructor"
     */
    @Override
    public RiscvOperand copy() {
        var props = this.getProperties().copy();
        return new RiscvOperand(props, this.getValue().intValue());
    }
}
