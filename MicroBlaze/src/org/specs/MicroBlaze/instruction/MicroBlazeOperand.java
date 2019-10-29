package org.specs.MicroBlaze.instruction;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmField;

import pt.up.fe.specs.binarytranslation.instruction.AOperand;
import pt.up.fe.specs.binarytranslation.instruction.AOperandProperties;

public class MicroBlazeOperand extends AOperand {

    public MicroBlazeOperand(MicroBlazeOperandPropertiesData pdata,
            MicroBlazeAsmField field, Integer value) {
        super(new AOperandProperties(pdata, field), value);
    }

    /*  public MicroBlazeOperand newReadRegister() {
        
        return new
    }*/

    /*
     * Copy "constructor"
     */
    @Override
    public MicroBlazeOperand copy() {
        return new MicroBlazeOperand(
                (MicroBlazeOperandProperties) this.props,
                (MicroBlazeAsmField) this.asmfield,
                Integer.valueOf(this.value));
    }
}
