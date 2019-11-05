package org.specs.Arm.parsing;

import static org.specs.Arm.instruction.ArmOperand.*;
import static org.specs.Arm.parsing.ArmAsmField.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.Arm.instruction.ArmInstructionCondition;
import org.specs.Arm.instruction.ArmInstructionShift;
import org.specs.Arm.instruction.ArmInstructionExtend;

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
        return ArmInstructionCondition.decodeCondition(condcode);
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
     * returns position of higher most bit at "1"
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
            Integer fullimm = (((imm << 2) | (imml)) << shift) * 4096;
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
            var Nval = map.get(N);
            Number fullimm = DecodeBitMasks(Nval, imms, immr, true);
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
            // first operand
            Number fullimm = map.get(IMM) << 2;
            operands.add(newImmediateLabel(IMM, fullimm, 64));
            break;
        }

        case EXCEPTION: {
            // first operand
            Number fullimm = map.get(IMM);
            operands.add(newImmediateLabel(IMM, fullimm, 16));
            break;
        }

        case UCONDITIONALBRANCH_REG: {
            // first operand
            operands.add(newReadRegister(RN, map.get(RN), 64));

            // for brab and braa
            var opa = map.get(OPCODEA); // contains bit "Z"
            if ((opa & 0x0001) != 0) {
                operands.add(newReadRegister(RM, map.get(OPCODED), 64));
            }

            break;
        }

        case UCONDITIONALBRANCH_IMM: {
            // first operand
            Number fullimm = map.get(IMM) << 2;
            operands.add(newImmediateLabel(IMM, fullimm, 64));
            break;
        }

        case COMPARE_AND_BRANCH_IMM: {
            // first operand
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RT, map.get(RT), wd));

            // second operand
            Number fullimm = map.get(IMM) << 2;
            operands.add(newImmediateLabel(IMM, fullimm, 64));
            break;
        }

        case TEST_AND_BRANCH: {
            // first operand
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newReadRegister(RT, map.get(RT), wd));

            // second operand
            var b5 = map.get(SF);
            var b40 = map.get(RM);
            operands.add(newImmediate(RM, ((b5 << 5) | b40), 8));

            // third operand
            Number label = map.get(IMM) << 2;
            operands.add(newImmediateLabel(IMM, label, 64));
            break;
        }

        // ldr literal loads, scalar and simd
        case LOAD_REG_LITERAL_FMT1: {

            var sf = map.get(SF);
            var simd = map.get(SIMD);
            int wd = 32 * (int) Math.pow(2, sf);

            // first operand
            if (simd == 0) {
                operands.add(newWriteRegister(RT, map.get(RT), wd));
            } else {
                operands.add(newSIMDWriteRegister(RT, map.get(RT), wd));
            }

            // TODO need addr of current instruction...

            // second operand
            var imm = map.get(IMM) << 2;
            Number label = (imm << (64 - 19)) >> (64 - 19);
            operands.add(newImmediateLabel(IMM, label, 64));
            break;
        }

        // should only be for "prfm" and "ldrsw_reg"
        case LOAD_REG_LITERAL_FMT2: {

            // first operand
            var wd = (map.get(OPCODEA) != 0) ? 64 : 32;
            operands.add(newWriteRegister(RT, map.get(RT), wd));

            // second operand
            Number label = map.get(IMM) << 2;
            operands.add(newImmediate(IMM, label, 32));
            break;
        }

        // stnp and ldnp (scalar and simd)
        case LOAD_STORE_PAIR_NO_ALLOC:
        case LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1: {

            var sf = map.get(SF);
            var simd = map.get(SIMD);
            if (simd == 0)
                sf = sf >> 1;

            int wd = 32 * (int) Math.pow(2, sf);

            // first and second operands
            if (simd == 0) {
                operands.add(newReadRegister(RT, map.get(RT), wd));
                operands.add(newReadRegister(RM, map.get(RM), wd));
            } else {
                operands.add(newSIMDReadRegister(RT, map.get(RT), wd));
                operands.add(newSIMDReadRegister(RM, map.get(RM), wd));
            }

            // third operand
            operands.add(newReadRegister(RN, map.get(RN), wd));

            // fourth (optional) operand
            var imm = map.get(IMM) * (wd / 8);
            Number fullimm = (imm << (64 - 7)) >> (64 - 7);

            if (type == ArmAsmFieldType.LOAD_STORE_PAIR_NO_ALLOC)
                wd = (wd == 32) ? 8 : 16;
            else // if LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1
                wd = 16;

            operands.add(newImmediate(IMM, fullimm, wd));
            break;
        }

        // stgp and ldpsw
        case LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2: {
            // first, second, and third operands
            operands.add(newReadRegister(RT, map.get(RT), 64));
            operands.add(newReadRegister(RM, map.get(RM), 64));
            operands.add(newReadRegister(RN, map.get(RN), 64));

            // fourth operand
            Number imm = map.get(IMM);
            operands.add(newImmediate(IMM, imm, 16));
            break;
        }

        // sturb, ldurb, sturh, ldurh, ldursw, prfum
        // ldursb, ldursh, stur, ldur
        case LOAD_STORE_PAIR_IMM_FMT1:
        case LOAD_STORE_PAIR_IMM_FMT2: {
            // first, and second operands
            var wd = (map.get(OPCODEA) != 0) ? 64 : 32;
            operands.add(newReadRegister(RT, map.get(RT), wd));
            operands.add(newReadRegister(RN, map.get(RN), 64));

            // third operand
            var imm = map.get(IMM);
            Number fullimm = (imm << (64 - 9)) >> (64 - 9);
            operands.add(newImmediate(IMM, fullimm, 64));
            break;
        }

        // stur (simd), ldur (simd)
        case LOAD_STORE_PAIR_IMM_FMT3: {
            break;
        }

        case LOAD_STORE_IMM_PREPOST_FMT1:
        case LOAD_STORE_IMM_PREPOST_FMT2:
        case LOAD_STORE_IMM_PREPOST_FMT3: {

            break;
        }

        case LOAD_STORE_REG_OFF_FMT1:
        case LOAD_STORE_REG_OFF_FMT2:
        case LOAD_STORE_REG_OFF_FMT3: {

            break;
        }

        case LOAD_STORE_REG_UIMM_FMT1:
        case LOAD_STORE_REG_UIMM_FMT2:
        case LOAD_STORE_REG_UIMM_FMT3: {

            break;
        }

        case DPR_TWOSOURCE:
        case ADD_SUB_CARRY: {
            // first, second, and third operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, map.get(RD), wd));
            operands.add(newReadRegister(RN, map.get(RN), wd));
            operands.add(newReadRegister(RM, map.get(RM), wd));
            break;
        }

        case DPR_ONESOURCE: {
            // first, and second operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, map.get(RD), wd));
            operands.add(newReadRegister(RN, map.get(RN), wd));
            break;
        }

        case LOGICAL_SHIFT_REG:
        case ADD_SUB_SHIFT_REG:
        case ADD_SUB_EXT_REG: {
            // first, second, and third operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, map.get(RD), wd));
            operands.add(newReadRegister(RN, map.get(RN), wd));
            operands.add(newReadRegister(RM, map.get(RM), wd));

            // fourth operand
            if (type != ArmAsmFieldType.ADD_SUB_EXT_REG) {
                var shift = ArmInstructionShift.decodeShift(map.get(SHIFT).intValue());
                operands.add(newSubOperation(SHIFT, shift.toString(), 8));

            } else {
                var ext = ArmInstructionExtend.decodeExtend(map.get(OPTION).intValue());
                operands.add(newSubOperation(OPTION, ext.toString(), 8));
            }

            // fifth operand (first suboperation operand)
            var imm6 = (map.get(IMM));
            operands.add(newImmediate(IMM, imm6, 8));

            // NOTE
            // there is a separate suboperation associated
            // with this operand.... see page C6-777 of ARMv8 ISA manual
            break;
        }

        case CONDITIONAL_CMP_REG:
        case CONDITIONAL_CMP_IMM: {
            // first operand
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newReadRegister(RN, map.get(RN), wd));

            // second operand
            if (type == ArmAsmFieldType.CONDITIONAL_CMP_REG)
                operands.add(newReadRegister(RM, map.get(RM), wd));
            else
                operands.add(newImmediate(IMM, map.get(IMM), 8));

            // third operand
            var nzcv = map.get(NZCV);
            operands.add(newImmediate(NZCV, nzcv, 8));

            // fourth operand
            var cond = map.get(COND);
            var conds = ArmInstructionCondition.decodeCondition(cond).getShorthandle();
            operands.add(newSubOperation(COND, conds, 8));
            break;
        }

        case CONDITIONAL_SELECT: {
            // first, second, and third operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, map.get(RD), wd));
            operands.add(newReadRegister(RN, map.get(RN), wd));
            operands.add(newReadRegister(RM, map.get(RM), wd));

            // fourth operand
            var cond = map.get(COND);
            var conds = ArmInstructionCondition.decodeCondition(cond).getShorthandle();
            operands.add(newSubOperation(COND, conds, 8));
            break;
        }

        case DPR_THREESOURCE: {
            // first, second, third, and fourth operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, map.get(RD), wd));
            operands.add(newReadRegister(RN, map.get(RN), wd));
            operands.add(newReadRegister(RM, map.get(RM), wd));
            operands.add(newReadRegister(RA, map.get(RA), wd));
            break;
        }

        default:
            break;
        }

        return operands;
    }
}
