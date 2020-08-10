package org.specs.MicroBlaze.instruction;

import static pt.up.fe.specs.binarytranslation.instruction.operand.OperandType.*;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmField;

import pt.up.fe.specs.binarytranslation.instruction.operand.*;

public class MicroBlazeOperand extends AOperand {

    /**
     * 
     */
    private static final long serialVersionUID = -1023924152110388490L;

    private MicroBlazeOperand(OperandProperties props, int value) {
        super(props, value);
    }

    public static MicroBlazeOperand newReadRegister(MicroBlazeAsmField field, int value) {
        var props = new AOperandProperties(field, "r", "", 32, REGISTER, READ, WORD);
        return new MicroBlazeOperand(props, value);
    }

    public static MicroBlazeOperand newWriteRegister(MicroBlazeAsmField field, int value) {
        var props = new AOperandProperties(field, "r", "", 32, REGISTER, WRITE, WORD);
        return new MicroBlazeOperand(props, value);
    }

    public static MicroBlazeOperand newImmediate(MicroBlazeAsmField field, int value) {
        var props = new AOperandProperties(field, "0x", "", 32, IMMEDIATE, READ, WORD);
        return new MicroBlazeOperand(props, value);
    }

    /*
     * Copy "constructor"
     */
    @Override
    public MicroBlazeOperand copy() {
        var props = this.getProperties().copy();
        return new MicroBlazeOperand(props, this.getNumberValue().intValue());
    }
}
