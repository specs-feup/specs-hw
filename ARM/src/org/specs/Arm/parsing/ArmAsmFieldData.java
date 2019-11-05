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

        ArmAsmFieldType type = (ArmAsmFieldType) this.get(TYPE);
        var map1 = this.get(FIELDS);

        // sf, sfa, and sfb fields are treated differently based on the instruction format
        switch (type) {

        // when "sf" is a single bit
        case DPI_ADDSUBIMM:
        case DPI_ADDSUBIMM_TAGS:
        case LOGICAL:
        case MOVEW:
        case EXTRACT:
        case BITFIELD:
        case DPR_TWOSOURCE:
        case LOGICAL_SHIFT_REG:
        case ADD_SUB_SHIFT_REG:
        case ADD_SUB_EXT_REG:
        case ADD_SUB_CARRY: {
            return (map.get(SF) != 0) ? 64 : 32;
        }

        // when sf is two bits
        case LOAD_REG_LITERAL_FMT1: {
            return ((int) Math.pow(2, map.get(SF))) * 32;
        }

        // when sf is two bits (again)
        case LOAD_STORE_PAIR_NO_ALLOC:
        case LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1: {

            var sf = map.get(SF);
            if (this.isSimd()) {
                sf = ((int) Math.pow(2, sf)) * 32;
            } else {
                sf = (sf != 0) ? 64 : 32;
            }
            return sf;
        }

        // fields "opcodea" and "opcodeb" used for size
        case LOAD_STORE_PAIR_IMM_FMT2: {
            Boolean a, b, c, d;
            a = map1.get("opcodea").substring(0, 1).equals("1");
            b = map1.get("opcodea").substring(1, 2).equals("1");
            c = map1.get("opcodeb").substring(0, 1).equals("1");
            d = map1.get("opcodeb").substring(1, 2).equals("1");
            var sf = ((!a & !d) | (b & !c)) ? 64 : 32;
            return sf;
        }

        // two fields, sfa, and sfb
        case LOAD_STORE_PAIR_IMM_FMT3:
        case LOAD_STORE_IMM_PREPOST_FMT3:
        case LOAD_STORE_REG_OFF_FMT3: {
            var sf = (map.get(SFB) << 2) | map.get(SFA);
            return ((int) Math.pow(2, sf)) * 8;
        }

        default:
            return 32;
        // TODO throw exception here??
        }
    }

    /*
     * Get "simd field"
     */
    public Boolean isSimd() {
        var map1 = this.get(FIELDS);
        if (!map.containsKey(SIMD)) {
            return false; // default length

        } else if (map.get(SIMD) != 0) {
            return true;

        } else {
            return false;
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
            return (map.get(IMM) << (32 - 9)) >> (32 - 9);

        default:
            return 0;
        // TODO throw exception here??
        }
    }

    /*
     * Gets a list of integers which represent the operands in the fields
     * This manner of field parsing, maintains the operand order as parsed
     * in the AsmFields
     */
    public List<Operand> getOperands() {
        return ArmAsmOperandGetter.getFrom(this);
    }
}
