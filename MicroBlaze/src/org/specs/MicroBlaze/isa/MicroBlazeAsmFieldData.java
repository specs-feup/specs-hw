package org.specs.MicroBlaze.isa;

import static org.specs.MicroBlaze.isa.MicroBlazeAsmFields.*;
import static org.specs.MicroBlaze.isa.MicroBlazeOperandProperties.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.Operand;
import pt.up.fe.specs.binarytranslation.asmparser.AsmFieldData;
import pt.up.fe.specs.binarytranslation.asmparser.AsmFieldType;

public class MicroBlazeAsmFieldData extends AsmFieldData {

    /*
     * Create raw
     */
    public MicroBlazeAsmFieldData(AsmFieldType type, Map<String, String> fields) {
        super(type, fields);
    }

    /*
     * Create from parent class
     */
    public MicroBlazeAsmFieldData(AsmFieldData fieldData) {
        super(fieldData.get(TYPE), fieldData.get(FIELDS));
    }

    /*
     * Copy "constructor"
     */
    public MicroBlazeAsmFieldData copy() {
        return new MicroBlazeAsmFieldData(this.getType(),
                new HashMap<String, String>(this.getFields()));
    }

    /*
     * Gets a list of integers which represent the operands in the fields
     * This manner of field parsing, maintains the operand order as parsed
     * in the AsmFields
     */
    public List<Operand> getOperands() {

        MicroBlazeAsmFieldType type = (MicroBlazeAsmFieldType) this.get(TYPE);
        var map1 = this.get(FIELDS);

        // get int values from fields
        Map<MicroBlazeAsmFields, Integer> operandmap = new HashMap<MicroBlazeAsmFields, Integer>();
        for (MicroBlazeAsmFields field : MicroBlazeAsmFields.values()) {
            if (map1.containsKey(field.getFieldName())) {
                operandmap.put(field, Integer.parseInt(map1.get(field.getFieldName()), 2));
            }
        }

        // assign to Operand objects based on field format
        List<Operand> operands = new ArrayList<Operand>();

        switch (type) {
        case TYPE_A:
            var rd = Integer.parseInt(map1.get(RD), 2);
            var ra = Integer.parseInt(map1.get(RA), 2);
            var rb = Integer.parseInt(map1.get(RB), 2);
            operands.add(new MicroBlazeOperand(register_write, operandmap.get(RD.getFieldName())));
            operands.add(new MicroBlazeOperand(register_read, ra));
            operands.add(new MicroBlazeOperand(register_read, rb));
            break;

        default:
            break;
        }

        return operands;
    }
}
