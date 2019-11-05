package org.specs.Arm.parsing;

import static org.specs.Arm.parsing.ArmAsmField.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.Arm.instruction.ArmInstructionCondition;
import org.specs.Arm.instruction.ArmInstructionExtend;
import org.specs.Arm.instruction.ArmInstructionShift;
import org.specs.Arm.instruction.ArmOperandBuilder;

import pt.up.fe.specs.binarytranslation.instruction.Operand;

public class ArmAsmOperandGetter {

    /*
     * map TYPE to a specific private operand init function (to avoid switch case)
     */
    interface ArmAsmOperandParse {
        void apply(ArmAsmFieldType tp, Map<ArmAsmField, Integer> map, ArmOperandBuilder h, List<Operand> operands);
    }

    private static final Map<ArmAsmFieldType, ArmAsmOperandParse> OPERANDGET;
    static {
        Map<ArmAsmFieldType, ArmAsmOperandParse> amap = new HashMap<ArmAsmFieldType, ArmAsmOperandParse>();
        amap.put(ArmAsmFieldType.DPI_PCREL, ArmAsmOperandGetter::dpi_pcrel);
        amap.put(ArmAsmFieldType.DPI_ADDSUBIMM, ArmAsmOperandGetter::dpi_addsubimm);
        amap.put(ArmAsmFieldType.LOGICAL, ArmAsmOperandGetter::logical);
        amap.put(ArmAsmFieldType.MOVEW, ArmAsmOperandGetter::movew);
        amap.put(ArmAsmFieldType.BITFIELD, ArmAsmOperandGetter::bitfield);
        amap.put(ArmAsmFieldType.EXTRACT, ArmAsmOperandGetter::extract);
        amap.put(ArmAsmFieldType.CONDITIONALBRANCH, ArmAsmOperandGetter::conditionalbranch);
        amap.put(ArmAsmFieldType.EXCEPTION, ArmAsmOperandGetter::exception);
        amap.put(ArmAsmFieldType.UCONDITIONALBRANCH_REG, ArmAsmOperandGetter::unconditionalbranchreg);
        amap.put(ArmAsmFieldType.UCONDITIONALBRANCH_IMM, ArmAsmOperandGetter::unconditionalbranchimm);
        amap.put(ArmAsmFieldType.COMPARE_AND_BRANCH_IMM, ArmAsmOperandGetter::compareandbranchimm);
        amap.put(ArmAsmFieldType.TEST_AND_BRANCH, ArmAsmOperandGetter::testandbranch);
        amap.put(ArmAsmFieldType.LOAD_REG_LITERAL_FMT1, ArmAsmOperandGetter::loadregliteralfmt1);
        amap.put(ArmAsmFieldType.LOAD_REG_LITERAL_FMT2, ArmAsmOperandGetter::loadregliteralfmt2);
        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_NO_ALLOC, ArmAsmOperandGetter::loadstore1);
        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1, ArmAsmOperandGetter::loadstore1);
        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2, ArmAsmOperandGetter::loadstore2);
        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_IMM_FMT1, ArmAsmOperandGetter::loadstore3);
        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_IMM_FMT2, ArmAsmOperandGetter::loadstore3);
        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_IMM_FMT3, ArmAsmOperandGetter::loadstore4);
        amap.put(ArmAsmFieldType.LOAD_STORE_IMM_PREPOST_FMT1, ArmAsmOperandGetter::loadstore4);
        amap.put(ArmAsmFieldType.LOAD_STORE_IMM_PREPOST_FMT2, ArmAsmOperandGetter::loadstore4);
        amap.put(ArmAsmFieldType.LOAD_STORE_IMM_PREPOST_FMT3, ArmAsmOperandGetter::loadstore4);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT1, ArmAsmOperandGetter::loadstore5);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT2, ArmAsmOperandGetter::loadstore5);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT3, ArmAsmOperandGetter::loadstore5);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_UIMM_FMT1, ArmAsmOperandGetter::loadstore6);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_UIMM_FMT1, ArmAsmOperandGetter::loadstore6);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_UIMM_FMT1, ArmAsmOperandGetter::loadstore6);
        amap.put(ArmAsmFieldType.DPR_TWOSOURCE, ArmAsmOperandGetter::dprTwoSource_addSubCarry);
        amap.put(ArmAsmFieldType.ADD_SUB_CARRY, ArmAsmOperandGetter::dprTwoSource_addSubCarry);
        amap.put(ArmAsmFieldType.DPR_ONESOURCE, ArmAsmOperandGetter::dprOneSource);
        amap.put(ArmAsmFieldType.LOGICAL_SHIFT_REG, ArmAsmOperandGetter::shift_ext_reg1);
        amap.put(ArmAsmFieldType.ADD_SUB_SHIFT_REG, ArmAsmOperandGetter::shift_ext_reg1);
        amap.put(ArmAsmFieldType.ADD_SUB_EXT_REG, ArmAsmOperandGetter::shift_ext_reg1);
        amap.put(ArmAsmFieldType.CONDITIONAL_CMP_REG, ArmAsmOperandGetter::conditionalCmp);
        amap.put(ArmAsmFieldType.CONDITIONAL_CMP_IMM, ArmAsmOperandGetter::conditionalCmp);
        amap.put(ArmAsmFieldType.CONDITIONAL_SELECT, ArmAsmOperandGetter::conditionalSel);
        amap.put(ArmAsmFieldType.DPR_THREESOURCE, ArmAsmOperandGetter::dprThreesource);

        OPERANDGET = Collections.unmodifiableMap(amap);
    }

    public static List<Operand> getFrom(ArmAsmFieldData fielddata) {
        var map = fielddata.getMap();
        var h = new ArmOperandBuilder(map);
        List<Operand> operands = new ArrayList<Operand>();
        var tp = (ArmAsmFieldType) fielddata.getType(); // type is guaranteed
        OPERANDGET.get(fielddata.get(ArmAsmFieldData.TYPE)).apply(tp, map, h, operands);
        return operands;
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
     * 
     */
    private long signExtend64(int value, int currlen) {
        return (value << (64 - currlen)) >> (64 - currlen);
    }

    /*
     * 
     */
    private int signExtend32(int value, int currlen) {
        return (value << (32 - currlen)) >> (32 - currlen);
    }

    private static void dpi_pcrel(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        operands.add(h.newWriteRegister(RD, 64));

        // build second operand from "imm" and "imml"
        var imml = map.get(IMML);
        var imm = map.get(IMM);
        var shift = (map.get(OPCODEA) == 0) ? 0 : 12;
        Integer fullimm = (((imm << 2) | (imml)) << shift) * 4096;
        operands.add(h.newImmediateLabel(IMM, fullimm, 64));
        return;
    }

    private static void dpi_addsubimm(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first and second operands
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));

        // third operand
        var imm = map.get(IMM);
        Number fullimm = (map.get(OPCODEB) == 1) ? imm << 12 : imm; // OPCODEB = "sh"
        operands.add(h.newImmediate(IMM, fullimm, wd));
        return;
    }

    private static void logical(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first and second operands
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));

        // third operand
        var immr = map.get(IMMR);
        var imms = map.get(IMMS);
        var Nval = map.get(N);
        Number fullimm = DecodeBitMasks(Nval, imms, immr, true);
        operands.add(h.newImmediate(IMM, fullimm, wd));
        return;
    }

    private static void movew(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newWriteRegister(RD, wd));

        // second operand
        var imm = map.get(IMM);
        var hw = map.get(OPCODEB); // OPCODEB = "hw"
        Number fullimm = imm << hw;
        operands.add(h.newImmediate(IMM, fullimm, wd));
        return;
    }

    private static void bitfield(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first and second operands
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));

        // 3rd and 4th
        operands.add(h.newImmediate(IMMR, 8)); // actually 6 bits
        operands.add(h.newImmediate(IMMS, 8)); // actually 6 bits
        return;
    }

    private static void extract(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, second and third operands
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));
        operands.add(h.newReadRegister(RM, wd));

        // fourth operand
        operands.add(h.newImmediate(IMMS, 8)); // actually 6 bits
        return;
    }

    private static void conditionalbranch(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        Number fullimm = map.get(IMM) << 2;
        operands.add(h.newImmediateLabel(IMM, fullimm, 64));
        return;
    }

    private static void exception(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        Number fullimm = map.get(IMM);
        operands.add(h.newImmediateLabel(IMM, fullimm, 16));
        return;
    }

    private static void unconditionalbranchreg(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        operands.add(h.newReadRegister(RN, 64));

        // for brab and braa
        var opa = map.get(OPCODEA); // contains bit "Z"
        if ((opa & 0x0001) != 0) {
            operands.add(h.newReadRegister(RM, map.get(OPCODED), 64));
        }
        return;
    }

    private static void unconditionalbranchimm(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        Number fullimm = map.get(IMM) << 2;
        operands.add(h.newImmediateLabel(IMM, fullimm, 64));
        return;
    }

    private static void compareandbranchimm(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newWriteRegister(RT, wd));

        // second operand
        Number fullimm = map.get(IMM) << 2;
        operands.add(h.newImmediateLabel(IMM, fullimm, 64));
        return;
    }

    private static void testandbranch(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newReadRegister(RT, wd));

        // second operand
        var b5 = map.get(SF);
        var b40 = map.get(RM);
        operands.add(h.newImmediate(RM, ((b5 << 5) | b40), 8));

        // third operand
        Number label = map.get(IMM) << 2;
        operands.add(h.newImmediateLabel(IMM, label, 64));
        return;
    }

    private static void loadregliteralfmt1(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        var sf = map.get(SF);
        var simd = map.get(SIMD);
        int wd = 32 * (int) Math.pow(2, sf);

        // first operand
        var key = simd << 1;
        operands.add(h.newRegister(key, RT, wd));

        // TODO need addr of current instruction...

        // second operand
        var imm = map.get(IMM) << 2;
        Number label = (imm << (64 - 19)) >> (64 - 19);
        operands.add(h.newImmediateLabel(IMM, label, 64));
        return;
    }

    private static void loadregliteralfmt2(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        var wd = (map.get(OPCODEA) != 0) ? 64 : 32;
        operands.add(h.newWriteRegister(RT, wd));

        // second operand
        Number label = map.get(IMM) << 2;
        operands.add(h.newImmediate(IMM, label, 32));
        return;
    }

    private static void loadstore1(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // bitwidth
        var sf = map.get(SF);
        var simd = map.get(SIMD);
        if (simd == 0)
            sf = sf >> 1;

        int wd = 32 * (int) Math.pow(2, sf);

        // load or store?
        var isload = (map.get(OPCODEA));
        var key = simd << 1 | isload;

        // first and second operands
        operands.add(h.newRegister(key, RT, wd));
        operands.add(h.newRegister(key, RM, wd));

        // third operand
        operands.add(h.newReadRegister(RN, wd));

        // fourth (optional) operand
        var imm = map.get(IMM) * (wd / 8);
        Number fullimm = (imm << (64 - 7)) >> (64 - 7);

        if (tp == ArmAsmFieldType.LOAD_STORE_PAIR_NO_ALLOC)
            wd = (wd == 32) ? 8 : 16;
        else // if LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1
            wd = 16;

        operands.add(h.newImmediate(IMM, fullimm, wd));
        return;
    }

    private static void loadstore2(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, second, and third operands
        operands.add(h.newReadRegister(RT, 64));
        operands.add(h.newReadRegister(RM, 64));
        operands.add(h.newReadRegister(RN, 64));

        // fourth operand
        operands.add(h.newImmediate(IMM, 16));
        return;
    }

    private static void loadstore3(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, and second operands
        var wd = (map.get(OPCODEA) != 0) ? 64 : 32;
        operands.add(h.newReadRegister(RT, wd));
        operands.add(h.newReadRegister(RN, 64));

        // third operand
        var imm = map.get(IMM);
        Number fullimm = (imm << (64 - 9)) >> (64 - 9);
        operands.add(h.newImmediate(IMM, fullimm, 64));
        return;
    }

    private static void loadstore4(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        int wd = 0;
        if (map.containsKey(SIMD)) {
            var sfa = map.get(SFA);
            var sfb = map.get(SFB);
            var sf = (sfb << 2) | sfa;
            wd = 32 * (int) Math.pow(2, sf);

        } else {
            wd = 32;
        }

        // first, and second operands
        operands.add(h.newRegister(0b10, RT, map.get(RT), wd));
        operands.add(h.newReadRegister(RN, map.get(RN), 64));

        // third operand
        var imm = map.get(IMM);
        Number fullimm = (imm << (16 - 9)) >> (16 - 9);
        operands.add(h.newImmediate(IMM, fullimm, 16));
        return;
    }

    private static void loadstore5(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        var isload = (map.get(OPCODEB));
        operands.add(h.newRegister(isload, RT, 32));

        // second operand
        operands.add(h.newReadRegister(RN, 64));

        // third operand
        var wd = ((map.get(OPTION) & 0b001) == 0) ? 32 : 64;
        operands.add(h.newReadRegister(RM, wd));

        // fourth operand
        var option = map.get(OPTION);
        if (option == 0b011) {
            var shift = ArmInstructionShift.decodeShift(map.get(SHIFT).intValue());
            operands.add(h.newSubOperation(SHIFT, shift.toString(), 8));

        } else {
            var ext = ArmInstructionExtend.decodeExtend(map.get(OPTION).intValue());
            operands.add(h.newSubOperation(OPTION, ext.toString(), 8));
        }

        // fifth operand
        operands.add(h.newImmediate(S, 16));
        return;
    }

    private static void loadstore6(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        return;
    }

    private static void dprTwoSource_addSubCarry(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, second, and third operands
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));
        operands.add(h.newReadRegister(RM, wd));
        return;
    }

    private static void dprOneSource(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, and second operands
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));
        return;
    }

    private static void shift_ext_reg1(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, second, and third operands
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));
        operands.add(h.newReadRegister(RM, wd));

        // fourth operand
        // NOTE
        // there is a separate suboperation associated
        // with this operand.... see page C6-777 of ARMv8 ISA manual
        if (tp != ArmAsmFieldType.ADD_SUB_EXT_REG) {
            var shift = ArmInstructionShift.decodeShift(map.get(SHIFT).intValue());
            operands.add(h.newSubOperation(SHIFT, shift.toString(), 8));

        } else {
            var ext = ArmInstructionExtend.decodeExtend(map.get(OPTION).intValue());
            operands.add(h.newSubOperation(OPTION, ext.toString(), 8));
        }

        // fifth operand (first suboperation operand)
        operands.add(h.newImmediate(IMM, 8));
        return;
    }

    private static void conditionalCmp(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newReadRegister(RN, wd));

        // second operand
        if (tp == ArmAsmFieldType.CONDITIONAL_CMP_REG)
            operands.add(h.newReadRegister(RM, wd));
        else
            operands.add(h.newImmediate(IMM, 8));

        // third operand
        operands.add(h.newImmediate(NZCV, 8));

        // fourth operand
        var cond = map.get(COND);
        var conds = ArmInstructionCondition.decodeCondition(cond).getShorthandle();
        operands.add(h.newSubOperation(COND, conds, 8));
        return;
    }

    private static void conditionalSel(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, second, and third operands
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));
        operands.add(h.newReadRegister(RM, wd));

        // fourth operand
        var cond = map.get(COND);
        var conds = ArmInstructionCondition.decodeCondition(cond).getShorthandle();
        operands.add(h.newSubOperation(COND, conds, 8));
        return;
    }

    private static void dprThreesource(ArmAsmFieldType tp,
            Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {
        // first, second, third, and fourth operands
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));
        operands.add(h.newReadRegister(RM, wd));
        operands.add(h.newReadRegister(RA, wd));
        return;
    }

}
