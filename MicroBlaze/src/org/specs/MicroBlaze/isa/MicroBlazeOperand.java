package org.specs.MicroBlaze.isa;

import pt.up.fe.specs.binarytranslation.generic.AOperand;

public class MicroBlazeOperand extends AOperand {

    public MicroBlazeOperand(MicroBlazeOperandProperties props, Integer value) {
        super(props, value);
    }

    @Override
    public void setSymbolic(String value) {
        if (this.props == MicroBlazeOperandProperties.register)
            this.props = MicroBlazeOperandProperties.symbolic_register;

        else if (this.props == MicroBlazeOperandProperties.immediate)
            this.props = MicroBlazeOperandProperties.symbolic_immediate;

        this.svalue = value;
        this.value = -1;
    }
}
