package org.specs.MicroBlaze.isa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.InstructionOperand;
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
     * Gets a list of integers which represent the operands in the fields
     */
    public List<InstructionOperand> getOperands() {

        var map1 = this.get(FIELDS);
        var keys1 = map1.keySet();
        List<InstructionOperand> operands = new ArrayList<InstructionOperand>();

        for (String key : keys1) {
            if (key.contains("register")) {
                var op = new MicroBlazeInstructionOperand(MicroBlazeInstructionOperandType.register,
                        Integer.parseInt(map1.get(key), 2));
                operands.add(op);

            } else if (key.contains("imm")) {
                var op = new MicroBlazeInstructionOperand(MicroBlazeInstructionOperandType.immediate,
                        Integer.parseInt(map1.get(key), 2));
                operands.add(op);
            }
        }

        return operands;
    }

}
