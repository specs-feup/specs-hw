package org.specs.Arm.isa;

import java.math.BigInteger;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;

public class ArmAsmInstructionData extends AsmInstructionData {

    /*
     * Create raw
     */
    public ArmAsmInstructionData(AsmInstructionType type, Map<String, String> fields) {
        super(type, fields);
    }

    /*
     * Create from parent class
     */
    public ArmAsmInstructionData(AsmInstructionData fieldData) {
        super(fieldData.get(TYPE), fieldData.get(FIELDS));
    }

    /*
     * Get "sf" field from ARM instruction types and interpret
     */
    public int getBitWidth() {

        ArmInstructionType type = (ArmInstructionType) this.get(TYPE);
        var map1 = this.get(FIELDS);
        var keys1 = map1.keySet();
        int sf = 0;

        if (!keys1.contains("sf") && !keys1.contains("sfa") && !keys1.contains("sfb")) {
            return 32; // default length
        }

        // sf, sfa, and sfb fields are treated differently based on the instruction format
        switch (type) {

        // when "sf" is a single bit
        case DPI_PCREL:
        case DPI_ADDSUBIMM:
        case DPI_ADDSUBIMM_TAGS:
        case LOGICAL:
        case MOVEW:
        case EXTRACT:
        case BITFIELD:
            return (map1.get("sf").equals("1")) ? 64 : 32;

        // when sf is two bits
        case LOAD_REG_LITERAL_FMT1:
            sf = Integer.parseInt(map1.get("sf"), 2);
            return ((int) Math.pow(2, sf)) * 32;

        // when sf is two bits (again)
        case LOAD_STORE_PAIR_NO_ALLOC:
        case LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1:

            sf = Integer.parseInt(map1.get("sf"), 2);
            if (this.isSimd()) {
                return ((int) Math.pow(2, sf)) * 32;
            } else {
                return (sf != 0) ? 64 : 32;
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
        var keys1 = map1.keySet();

        if (!keys1.contains("simd")) {
            return false; // default length

        } else if (map1.get("simd").equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Decode "cond" field if present
     */
    public ArmInstructionCondition getCond() {
        var map1 = this.get(FIELDS);
        var keys1 = map1.keySet();

        if (!keys1.contains("cond"))
            return ArmInstructionCondition.NONE;

        int condcode = Integer.parseInt(map1.get("cond"), 2);
        for (ArmInstructionCondition cond : ArmInstructionCondition.values()) {
            if (cond.getCondCode() == condcode)
                return cond;
        }
        return ArmInstructionCondition.NONE;
        // TODO throw something
    }

    /*
     * Repeat a bit (as string) "n" times (for sign extension of imm fields)
     */
    private String repeat(String bit, int n) {
        String extension = "";
        for (int i = 0; i < n; i++)
            extension = extension + bit;
        return extension;
    }

    /*
     * Get target of branch if instruction is branch
     */
    public int getBranchTarget() {

        ArmInstructionType type = (ArmInstructionType) this.get(TYPE);
        var map1 = this.get(FIELDS);
        var keys1 = map1.keySet();
        int imm = 0;

        switch (type) {

        // conditional branches have a 19 bit IMM field
        case CONDITIONALBRANCH:
            String immfield = map1.get("imm");
            immfield = repeat(immfield.substring(0, 1), 32 - immfield.length()) + immfield;
            imm = new BigInteger(immfield, 2).intValue();
            return imm;

        default:
            return 0;
        // TODO throw exception here??
        }

    }
}
