package org.specs.MicroBlaze.isa;

import pt.up.fe.specs.binarytranslation.generic.AOperand;

public class MicroBlazeOperand extends AOperand {

    public MicroBlazeOperand(MicroBlazeOperandProperties props, Integer value) {
        super(props, value);
    }

    @Override
    public void setSymbolic(String value) {
        if (this.props == MicroBlazeOperandProperties.register_read)
            this.props = MicroBlazeOperandProperties.symbolic_register_read;

        else if (this.props == MicroBlazeOperandProperties.register_write)
            this.props = MicroBlazeOperandProperties.symbolic_register_write;

        else if (this.props == MicroBlazeOperandProperties.immediate)
            this.props = MicroBlazeOperandProperties.symbolic_immediate;

        this.svalue = value;
        this.value = -1;
    }

    /*
     * Copy "constructor"
     */
    @Override
    public MicroBlazeOperand copy() {
        return new MicroBlazeOperand(
                (MicroBlazeOperandProperties) this.props, Integer.valueOf(this.value));
    }
}
