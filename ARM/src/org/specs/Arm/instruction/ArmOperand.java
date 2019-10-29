package org.specs.Arm.instruction;

import pt.up.fe.specs.binarytranslation.instruction.AOperand;
import pt.up.fe.specs.binarytranslation.instruction.OperandProperties;

public class ArmOperand extends AOperand {

    /*
     * 
     */
    private ArmOperand(OperandProperties props, Integer value) {
        super(props, value);
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
