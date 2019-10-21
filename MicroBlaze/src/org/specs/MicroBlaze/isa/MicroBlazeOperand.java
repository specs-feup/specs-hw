package org.specs.MicroBlaze.isa;

import pt.up.fe.specs.binarytranslation.generic.AOperand;

public class MicroBlazeOperand extends AOperand {

    public MicroBlazeOperand(MicroBlazeOperandProperties props, Integer value) {
        super(props, value);
    }

    @Override
    public void setVague() {
        if (this.props == MicroBlazeOperandProperties.register)
            this.props = MicroBlazeOperandProperties.vague_register;

        else if (this.props == MicroBlazeOperandProperties.immediate)
            this.props = MicroBlazeOperandProperties.vague_immediate;
    }
}
