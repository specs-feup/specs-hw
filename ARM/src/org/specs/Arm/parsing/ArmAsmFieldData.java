package org.specs.Arm.parsing;

import static org.specs.Arm.parsing.ArmAsmField.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.Arm.instruction.ArmInstructionCondition;

import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;

public class ArmAsmFieldData extends AsmFieldData {

    /*
     * remmaping of <string, string> to <asmfield, string>
     */
    private final Map<ArmAsmField, Integer> map = new HashMap<ArmAsmField, Integer>();

    /*
     * Create raw
     */
    public ArmAsmFieldData(AsmFieldType type, Map<String, String> fields) {
        super(type, fields);

        // get int values from fields
        var map1 = this.get(FIELDS);
        for (ArmAsmField field : ArmAsmField.values()) {
            if (map1.containsKey(field.getFieldName())) {
                map.put(field, Integer.parseInt(map1.get(field.getFieldName()), 2));
            }
        }
    }

    /*
     * Create from parent class
     */
    public ArmAsmFieldData(AsmFieldData fieldData) {
        this(fieldData.get(TYPE), fieldData.get(FIELDS));
    }

    /*
     * 
     */
    public Map<ArmAsmField, Integer> getMap() {
        return map;
    }

    /*
     * Get "sf" field from ARM instruction types and interpret
     */
    public int getBitWidth() {
        return ArmAsmWidthGetter.getFrom(this);
    }

    /*
     * Gets a list of integers which represent the operands in the fields
     * This manner of field parsing, maintains the operand order as parsed
     * in the AsmFields
     */
    public List<Operand> getOperands() {
        return ArmAsmOperandGetter.getFrom(this);
    }

    /*
     * Get "simd field"
     */
    public Boolean isSimd() {
        if (!map.containsKey(SIMD)) {
            return false;

        } else {
            return (map.get(SIMD) == 0);
        }
    }

    /*
     * Decode "cond" field if present
     */
    public ArmInstructionCondition getCond() {
        if (!map.containsKey(COND))
            return ArmInstructionCondition.NONE;

        return ArmInstructionCondition.decodeCondition(map.get(COND));
        // TODO throw something
    }

    /*
    * Get target of branch if instruction is branch
    */
    public int getBranchTarget() {

        ArmAsmFieldType type = (ArmAsmFieldType) this.get(TYPE);

        switch (type) {

        // conditional branches have a 19 bit IMM field
        case CONDITIONALBRANCH:
            return ((map.get(IMM) << 2) << (32 - 19)) >> (32 - 19);

        default:
            return 0;
        // TODO throw exception here??
        }
    }

}
