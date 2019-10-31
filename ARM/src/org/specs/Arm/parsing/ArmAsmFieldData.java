package org.specs.Arm.parsing;

import static org.specs.Arm.instruction.ArmOperand.*;
import static org.specs.Arm.parsing.ArmAsmField.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.Arm.instruction.ArmInstructionCondition;

import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;

public class ArmAsmFieldData extends AsmFieldData {

    /*
     * Create raw
     */
    public ArmAsmFieldData(AsmFieldType type, Map<String, String> fields) {
        super(type, fields);
    }

    /*
     * Create from parent class
     */
    public ArmAsmFieldData(AsmFieldData fieldData) {
        super(fieldData.get(TYPE), fieldData.get(FIELDS));
    }

    /*
     * Get "sf" field from ARM instruction types and interpret
     */
    public int getBitWidth() {

        ArmAsmFieldType type = (ArmAsmFieldType) this.get(TYPE);
        var map1 = this.get(FIELDS);
        int sf = 0;

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
        case ADD_SUB_CARRY:
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
                sf = ((int) Math.pow(2, sf)) * 32;
            } else {
                sf = (sf != 0) ? 64 : 32;
            }
            return sf;

        // fields "opcodea" and "opcodeb" used for size
        case LOAD_STORE_PAIR_IMM_FMT2:
            Boolean a, b, c, d;
            a = map1.get("opcodea").substring(0, 1).equals("1");
            b = map1.get("opcodea").substring(1, 2).equals("1");
            c = map1.get("opcodeb").substring(0, 1).equals("1");
            d = map1.get("opcodeb").substring(1, 2).equals("1");
            sf = ((!a & !d) | (b & !c)) ? 64 : 32;
            return sf;

        // two fields, sfa, and sfb
        case LOAD_STORE_PAIR_IMM_FMT3:
        case LOAD_STORE_IMM_PREPOST_FMT3:
        case LOAD_STORE_REG_OFF_FMT3:
            sf = Integer.parseInt(map1.get("sfb") + map1.get("sfa"), 2);
            return ((int) Math.pow(2, sf)) * 8;

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
    private static String repeat(String bit, int n) {
        String extension = "";
        for (int i = 0; i < n; i++)
            extension = extension + bit;
        return extension;
    }

    /*
     * 
     */
    private static long replicate(long mask, int e) {
        while (e < 64) {
            mask |= mask << e;
            e *= 2;
        }
        return mask;
    }

    /*
     * returns position of highermost bit at "1"
     */
    private static int leadingbit(long x) {
        int nr = 0;
        while (x > 0) {
            x = x >> 1;
            nr++;
        }
        return nr - 1;
    }

    /*
     * Set the bottom most "len" bits
     */
    private static long bitmask(long len) {
        return -1L >>> (64 - len);
    }

    /*
     * implements the pseudo-code in page 7389 of the armv8 instruction manual:
     * https://static.docs.arm.com/ddi0487/ea/DDI0487E_a_armv8_arm.pdf
     */
    private static long DecodeBitMasks(long N, long imms, long immr, boolean imm) {

        var aux = (N << 6) | (~imms & 0x3F);
        var len = leadingbit(aux);
        var esize = 1 << len;
        var levels = esize - 1;
        var S = imms & levels;
        var R = immr & levels;

        var wmask = bitmask(S + 1);
        if (R != 0) {
            wmask = (wmask >> R) | (wmask << (esize - R));
            wmask &= bitmask(esize);
        }
        wmask = replicate(wmask, esize);
        return wmask;
    }

    /*
     * Get target of branch if instruction is branch
     */
    public int getBranchTarget() {

        ArmAsmFieldType type = (ArmAsmFieldType) this.get(TYPE);
        var map1 = this.get(FIELDS);

        switch (type) {

        // conditional branches have a 19 bit IMM field
        case CONDITIONALBRANCH:
            String immfield = map1.get("imm");
            immfield = repeat(immfield.substring(0, 1), 32 - immfield.length()) + immfield;
            var imm = new BigInteger(immfield, 2).intValue();
            return imm;

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

        ArmAsmFieldType type = (ArmAsmFieldType) this.get(TYPE);
        var map1 = this.get(FIELDS);

        // get int values from fields
        Map<ArmAsmField, Integer> map = new HashMap<ArmAsmField, Integer>();
        for (ArmAsmField field : ArmAsmField.values()) {
            if (map1.containsKey(field.getFieldName())) {
                map.put(field, Integer.parseInt(map1.get(field.getFieldName()), 2));
            }
        }

        // assign to Operand objects based on field format
        List<Operand> operands = new ArrayList<Operand>();

        // order of operands MUST be preserved (or should be)
        switch (type) {

        ///////////////////////////////////////////////////////////////////////
        case DPI_PCREL: {
            // first operand
            operands.add(newWriteRegister(RD, map.get(RD), 64));

            // build second operand from "imm" and "imml"
            var imml = map.get(IMML);
            var imm = map.get(IMM);
            var shift = (map.get(OPCODEA) == 0) ? 0 : 12;
            Integer fullimm = ((imm << (2 + shift)) | (imml << shift));
            operands.add(newImmediateLabel(IMM, fullimm, 64));
            break;
        }

        ///////////////////////////////////////////////////////////////////////
        case DPI_ADDSUBIMM: {
            // first and second operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, map.get(RD), wd));
            operands.add(newReadRegister(RN, map.get(RN), wd));

            // third operand
            var imm = map.get(IMM);
            Number fullimm = (map.get(OPCODEB) == 1) ? imm << 12 : imm; // OPCODEB = "sh"
            operands.add(newImmediate(IMM, fullimm, wd));
            break;
        }

        /* TODO: depends on architectural configuration (and is confusing...)
        case DPI_ADDSUBIMM_TAGS: {
            // first and second operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, map.get(RD), wd));
            operands.add(newWriteRegister(RN, map.get(RN), wd));
            
            
            break;
        }
        */

        case LOGICAL: {
            // first and second operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, map.get(RD), wd));
            operands.add(newReadRegister(RN, map.get(RN), wd));

            // third operand
            var immr = map.get(IMMR);
            var imms = map.get(IMMS);
            var N = map.get(OPCODEB);
            Number fullimm = DecodeBitMasks(N, imms, immr, true);
            operands.add(newImmediate(IMM, fullimm, wd));
            break;
        }

        case MOVEW: {
            // first operand
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, map.get(RD), wd));

            var imm = map.get(IMM);
            var hw = map.get(OPCODEB); // OPCODEB = "hw"
            Number fullimm = imm << hw;
            operands.add(newImmediate(IMM, fullimm, wd));
            break;
        }

        case BITFIELD: {
            // first and second operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, map.get(RD), wd));
            operands.add(newReadRegister(RN, map.get(RN), wd));

            // 3rd and 4th
            operands.add(newImmediate(IMMR, map.get(IMMR), 8)); // actually 6 bits
            operands.add(newImmediate(IMMS, map.get(IMMS), 8)); // actually 6 bits
            break;
        }

        case EXTRACT: {
            // first, second and third operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, map.get(RD), wd));
            operands.add(newReadRegister(RN, map.get(RN), wd));
            operands.add(newReadRegister(RM, map.get(RM), wd));

            // fourth operand
            operands.add(newImmediate(IMMS, map.get(IMMS), 8)); // actually 6 bits
            break;
        }

        case CONDITIONALBRANCH: {

            break;
        }

        default:
            break;
        }

        return operands;
    }
}
