package org.specs.Arm.parsing;

import static org.specs.Arm.parsing.ArmAsmField.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.Arm.instruction.ArmInstructionCondition;

import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;

public class ArmAsmFieldData extends AsmFieldData {

    /**
     * 
     */
    private static final long serialVersionUID = 62818512602539823L;

    /*
     * remmaping of <string, string> to <asmfield, string>
     */
    private final Map<ArmAsmField, Integer> map = new HashMap<ArmAsmField, Integer>();

    /*
     * Create raw
     */
    public ArmAsmFieldData(Number addr, AsmFieldType type, Map<String, String> fields) {
        super(addr, type, fields);

        // get int values from fields
        for (ArmAsmField field : ArmAsmField.values()) {
            if (fields.containsKey(field.getFieldName())) {
                map.put(field, Integer.parseInt(fields.get(field.getFieldName()), 2));
            }
        }
    }

    /*
     * Create from parent class
     */
    public ArmAsmFieldData(AsmFieldData fieldData) {
        this(fieldData.get(ADDR), fieldData.get(TYPE), fieldData.get(FIELDS));
    }

    /*
     * Copy "constructor"
     */
    public ArmAsmFieldData copy() {
        return new ArmAsmFieldData(
                this.get(ADDR), this.getType(),
                new HashMap<String, String>(this.getFields()));
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
        if (map.containsKey(SIMD)) {
            return (map.get(SIMD) != 0);

        } else {
            return false;
        }
    }

    /*
     * Decode "cond" field if present
     */
    public ArmInstructionCondition getCond() {
        if (map.containsKey(COND))
            return ArmInstructionCondition.decodeCondition(map.get(COND));

        return ArmInstructionCondition.NONE;
        // TODO throw something
    }

    /*
    * Get target of branch if instruction is branch
    */
    public Number getBranchTarget() {
        return ArmAsmBranchTargetGetter.getFrom(this);
    }
}
