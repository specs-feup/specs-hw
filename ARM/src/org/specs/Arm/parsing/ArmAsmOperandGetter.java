package org.specs.Arm.parsing;

import static org.specs.Arm.parsing.ArmAsmField.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.Arm.instruction.ArmOperandBuilder;

import pt.up.fe.specs.binarytranslation.instruction.Operand;

public class ArmAsmOperandGetter {

    /*
     * map TYPE to a specific private operand init function (to avoid switch case)
     */
    interface ArmAsmOperandParse {
        void apply(Map<ArmAsmField, Integer> map, ArmOperandBuilder h, List<Operand> operands);
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

        OPERANDGET = Collections.unmodifiableMap(amap);
    }

    public static List<Operand> getFrom(ArmAsmFieldData fielddata) {
        var map = fielddata.getMap();
        var h = new ArmOperandBuilder(map);
        List<Operand> operands = new ArrayList<Operand>();
        OPERANDGET.get(fielddata.get(ArmAsmFieldData.TYPE)).apply(map, h, operands);
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

    private static void dpi_pcrel(Map<ArmAsmField, Integer> map,
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

    private static void dpi_addsubimm(Map<ArmAsmField, Integer> map,
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

    private static void logical(Map<ArmAsmField, Integer> map,
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

    private static void movew(Map<ArmAsmField, Integer> map,
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

    private static void bitfield(Map<ArmAsmField, Integer> map,
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

    private static void extract(Map<ArmAsmField, Integer> map,
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

    private static void conditionalbranch(Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        Number fullimm = map.get(IMM) << 2;
        operands.add(h.newImmediateLabel(IMM, fullimm, 64));
        return;
    }

    private static void exception(Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        Number fullimm = map.get(IMM);
        operands.add(h.newImmediateLabel(IMM, fullimm, 16));
        return;
    }

    private static void unconditionalbranchreg(Map<ArmAsmField, Integer> map,
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

    private static void unconditionalbranchimm(Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        Number fullimm = map.get(IMM) << 2;
        operands.add(h.newImmediateLabel(IMM, fullimm, 64));
        return;
    }

    private static void compareandbranchimm(Map<ArmAsmField, Integer> map,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        var wd = (map.get(SF) == 1) ? 64 : 32;
        operands.add(h.newWriteRegister(RT, wd));

        // second operand
        Number fullimm = map.get(IMM) << 2;
        operands.add(h.newImmediateLabel(IMM, fullimm, 64));
        return;
    }

    private static void testandbranch(Map<ArmAsmField, Integer> map,
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
}
