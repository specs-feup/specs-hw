package org.specs.Arm.instruction;

import static pt.up.fe.specs.binarytranslation.instruction.OperandType.*;

import org.specs.Arm.parsing.ArmAsmField;

import pt.up.fe.specs.binarytranslation.instruction.AOperand;
import pt.up.fe.specs.binarytranslation.instruction.AOperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.OperandProperties;

public class ArmOperand extends AOperand {

    /*
     * 
     */
    private ArmOperand(OperandProperties props, Integer value) {
        super(props, value);
    }

    public static ArmOperand newReadRegister64(ArmAsmField field, Integer value) {
        var props = new AOperandProperties(field, "X", "", 64, REGISTER, READ, DWORD);
        return new ArmOperand(props, value);
    }

    public static ArmOperand newWriteRegister64(ArmAsmField field, Integer value) {
        var props = new AOperandProperties(field, "X", "", 64, REGISTER, WRITE, DWORD);
        return new ArmOperand(props, value);
    }

    public static ArmOperand newImmediate64(ArmAsmField field, Integer value) {
        var props = new AOperandProperties(field, "", "", 64, IMMEDIATE, READ, DWORD);
        return new ArmOperand(props, value);
    }

    // TODO make public methods newReadRegister, and newWriteRegister, with a data width argument
    // and call newReadRegister32, or 64 only here, after parsing that argument

    /*
     * Copy "constructor"
     */
    @Override
    public ArmOperand copy() {
        var props = this.getProperties().copy();
        return new ArmOperand(props, this.getIntegerValue().intValue());
    }
}
