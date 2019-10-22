package org.specs.MicroBlaze.isa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.Operand;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;

public class MicroBlazeAsmInstructionData extends AsmInstructionData {

    /*
     * Create raw
     */
    public MicroBlazeAsmInstructionData(AsmInstructionType type, Map<String, String> fields) {
        super(type, fields);
    }

    /*
     * Create from parent class
     */
    public MicroBlazeAsmInstructionData(AsmInstructionData fieldData) {
        super(fieldData.get(TYPE), fieldData.get(FIELDS));
    }

    /*
     * Copy "constructor"
     */
    public MicroBlazeAsmInstructionData copy() {
        return new MicroBlazeAsmInstructionData(this.getType(),
                new HashMap<String, String>(this.getFields()));
    }

    /*
     * Gets a list of integers which represent the operands in the fields
     */
    public List<Operand> getOperands() {

        var map1 = this.get(FIELDS);
        var keys1 = map1.keySet();
        List<Operand> operands = new ArrayList<Operand>();

        for (String key : keys1) {
            if (key.contains("register")) {
                var op = new MicroBlazeOperand(MicroBlazeOperandProperties.register,
                        Integer.parseInt(map1.get(key), 2));
                operands.add(op);

            } else if (key.contains("imm")) {
                var op = new MicroBlazeOperand(MicroBlazeOperandProperties.immediate,
                        Integer.parseInt(map1.get(key), 2));
                operands.add(op);
            }
        }

        return operands;
    }
}
