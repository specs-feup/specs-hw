package org.specs.MicroBlaze.isa;

import static org.specs.MicroBlaze.isa.MicroBlazeOperandProperties.*;

import pt.up.fe.specs.binarytranslation.instruction.AOperand;

public class MicroBlazeOperand extends AOperand {

    public MicroBlazeOperand(MicroBlazeOperandProperties props, Integer value) {
        super(props, value);
    }

    @Override
    public void setSymbolic(String value) {
        if (this.props == register_read)
            this.props = symbolic_register_read;

        else if (this.props == register_write)
            this.props = symbolic_register_write;

        else if (this.props == immediate)
            this.props = symbolic_immediate;

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
