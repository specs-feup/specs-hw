package pt.up.fe.specs.binarytranslation.generic;

import pt.up.fe.specs.binarytranslation.InstructionOperand;
import pt.up.fe.specs.binarytranslation.InstructionOperandType;

public class GenericInstructionOperand implements InstructionOperand {

    private InstructionOperandType type;
    protected Integer value;

    public GenericInstructionOperand(InstructionOperandType type, Integer value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public Integer getType() {
        return this.getType();
    }

    @Override
    public String getRepresentation() {
        String ret = "";
        if (type == InstructionOperandType.register)
            ret = (type.getPrefix() + Integer.toString(this.value));

        else if (type == InstructionOperandType.immediate)
            ret = (type.getPrefix() + "0x" + Integer.toHexString(this.value));

        return ret;
    }
}
