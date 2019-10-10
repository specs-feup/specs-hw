package org.specs.Arm.isa;

import java.util.Map;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;

public class ArmAsmInstructionData extends AsmInstructionData {

    public ArmAsmInstructionData(AsmInstructionType type, Map<String, String> fields) {
        super(type, fields);
    }

    /*
     * Get "sf" field from ARM instruction types and interpret
     */
    @Override
    public int getBitWidth() {

        ArmInstructionType type = (ArmInstructionType) this.get(TYPE);
        var map1 = this.get(FIELDS);
        var keys1 = map1.keySet();

        if (!keys1.contains("sf") && !keys1.contains("sfa") && !keys1.contains("sfb")) {
            return 32; // default length
        }

        // sf, sfa, and sfb fields are treated differently based on the instruction format
        switch (type) {

        case DPI_PCREL:
        case DPI_ADDSUBIMM:
        case DPI_ADDSUBIMM_TAGS:
        case LOGICAL:
        case MOVEW:
        case EXTRACT:
        case BITFIELD:
            return (map1.get("sf").equals("1")) ? 64 : 32;

        default:
            return 32;
        // TODO throw exception here??
        }
    }

    /*
     * Get "simd field"
     */
    @Override
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
    public ArmInstructionConditions getCond() {
        var map1 = this.get(FIELDS);
        var keys1 = map1.keySet();

        if (!keys1.contains("cond"))
            return ArmInstructionConditions.NONE;

        int condcode = Integer.parseInt(map1.get("cond"), 2);
        for (ArmInstructionConditions cond : ArmInstructionConditions.values()) {
            if (cond.getCondCode() == condcode)
                return cond;
        }
        return ArmInstructionConditions.NONE;
        // TODO throw something
    }
}
