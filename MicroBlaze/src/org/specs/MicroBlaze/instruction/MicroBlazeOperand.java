package org.specs.MicroBlaze.instruction;

import static org.specs.MicroBlaze.instruction.MicroBlazeOperandProperties.immediate;
import static org.specs.MicroBlaze.instruction.MicroBlazeOperandProperties.register_read;
import static org.specs.MicroBlaze.instruction.MicroBlazeOperandProperties.register_write;
import static org.specs.MicroBlaze.instruction.MicroBlazeOperandProperties.symbolic_immediate;
import static org.specs.MicroBlaze.instruction.MicroBlazeOperandProperties.symbolic_register_read;
import static org.specs.MicroBlaze.instruction.MicroBlazeOperandProperties.symbolic_register_write;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmFields;

import pt.up.fe.specs.binarytranslation.instruction.AOperand;

public class MicroBlazeOperand extends AOperand {

    private MicroBlazeAsmFields thisfieldtype;
    
    public MicroBlazeOperand(MicroBlazeOperandProperties props, 
            MicroBlazeAsmFields thisfieldtype, Integer value) {
        super(props, value);
        this.thisfieldtype = thisfieldtype;
    }
    
    public MicroBlazeOperand(MicroBlazeOperandProperties props, Integer value) {
        super(props, value);
    }    
    
    public String getField() {
        return this.thisfieldtype.getFieldName();
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
