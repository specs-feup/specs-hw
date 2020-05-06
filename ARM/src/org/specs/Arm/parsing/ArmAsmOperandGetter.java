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
        List<Operand> apply(ArmAsmFieldData fielddata, ArmOperandBuilder h, List<Operand> operands);
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

        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT1, ArmAsmOperandGetter::loadstore3);
        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT2, ArmAsmOperandGetter::loadstore3);
        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT3, ArmAsmOperandGetter::loadstore3);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_IMM_PREPOST_FMT1, ArmAsmOperandGetter::loadstore3);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_IMM_PREPOST_FMT2, ArmAsmOperandGetter::loadstore3);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_IMM_PREPOST_FMT3, ArmAsmOperandGetter::loadstore3);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_UIMM_FMT1, ArmAsmOperandGetter::loadstore3);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_UIMM_FMT2, ArmAsmOperandGetter::loadstore3);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_UIMM_FMT3, ArmAsmOperandGetter::loadstore3);

        amap.put(ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT1, ArmAsmOperandGetter::loadstore5);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT2, ArmAsmOperandGetter::loadstore5);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT3, ArmAsmOperandGetter::loadstore5);

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
        amap.put(ArmAsmFieldType.UNDEFINED, ArmAsmOperandGetter::undefined);

        OPERANDGET = Collections.unmodifiableMap(amap);
    }

    /*
     * Required since some operations have implicit operands
     * which are the results of the previous instruction (e.g., conditional branches)
     */
    static List<Operand> previousInstructionOperands;

    public static List<Operand> getFrom(ArmAsmFieldData fielddata) {

        var h = new ArmOperandBuilder(fielddata.getMap());
        var func = OPERANDGET.get(fielddata.get(ArmAsmFieldData.TYPE));
        if (func == null)
            func = ArmAsmOperandGetter::undefined;

        var ops = func.apply(fielddata, h, new ArrayList<Operand>());
        previousInstructionOperands = ops;
        return ops;
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
    private static long signExtend64(int value, int currlen) {
        return (value << (64 - currlen)) >> (64 - currlen);
    }

    /*
     * 
     */
    private static int signExtend32(int value, int currlen) {
        return (value << (32 - currlen)) >> (32 - currlen);
    }

    /*
     * 
     */
    private static byte signExtend8(byte value, int currlen) {
        return (byte) ((value << (8 - currlen)) >> (8 - currlen));
    }

    /*
     * creates 2 bit mask for ArmOperand initialization based on 
     * isSIMD and isLoad/isStore values
     */
    private static int makeKey(ArmAsmFieldData fielddata) {

        int key = 0;
        var map = fielddata.getMap();
        var tp = (ArmAsmFieldType) fielddata.getType();
        // type guaranteed

        switch (tp) {

        case LOAD_REG_LITERAL_FMT1: {
            key = map.get(SIMD) << 1 | 0b01;
            break;
        }

        case LOAD_STORE_PAIR_NO_ALLOC:
        case LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1: {
            key = map.get(SIMD) << 1 | (map.get(OPCODEA));
            break;
        }

        case LOAD_STORE_REG_IMM_PREPOST_FMT1:
        case LOAD_STORE_REG_IMM_PREPOST_FMT2:
        case LOAD_STORE_REG_IMM_PREPOST_FMT3:
        case LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT1:
        case LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT2:
        case LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT3: {
            var isload = (map.get(OPCODEB) & 0b01);
            var issimd = map.get(SIMD) << 1;
            key = issimd | isload;
            break;
        }

        default: {
            key = 0b00;
            break;
        }
        }

        return key;
    }

    // DPI_PCREL (C4-253)
    private static List<Operand> dpi_pcrel(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var map = fielddata.getMap();

        // first operand
        operands.add(h.newWriteRegister(RD, 64));

        // build second operand from "imm" and "imml"
        var imml = map.get(IMML);
        var imm = map.get(IMM);
        var shift = (map.get(OPCODEA) == 0) ? 0 : 12;
        Integer fullimm = (((imm << 2) | (imml)) << shift) * 4096;
        operands.add(h.newImmediateLabel(IMM, fullimm, 64));

        // TODO adr needs instruction addr
        // TODO adpr needs instruction page addr

        return operands;
    }

    // DPI_ADDSUBIMM (C4-253 to C4-254)
    private static List<Operand> dpi_addsubimm(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var map = fielddata.getMap();
        var wd = fielddata.getBitWidth();

        // first and second operands
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));

        // third operand
        var imm = map.get(IMM);
        Number fullimm = (map.get(OPCODEB) == 1) ? imm << 12 : imm; // OPCODEB = "sh"
        operands.add(h.newImmediate(IMM, fullimm, wd));

        return operands;
    }

    // LOGICAL (C4-255)
    private static List<Operand> logical(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var map = fielddata.getMap();
        var wd = fielddata.getBitWidth();

        // first and second operands
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));

        // third operand
        var immr = map.get(IMMR);
        var imms = map.get(IMMS);
        var Nval = map.get(N);
        Number fullimm = DecodeBitMasks(Nval, imms, immr, true);
        operands.add(h.newImmediate(IMM, fullimm, wd));

        return operands;
    }

    // MOVEW (C4-255)
    private static List<Operand> movew(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var map = fielddata.getMap();
        var wd = fielddata.getBitWidth();

        // first operand
        operands.add(h.newWriteRegister(RD, wd));

        // second operand
        operands.add(h.newImmediate(IMM, wd));

        // if shift value is 0, then there is no LSL
        if (fielddata.getMap().get(OPCODEB).intValue() == 0)
            return operands;

        // third operand (always LSL)
        operands.add(h.newSubOperation(SHIFT, ArmInstructionShift.LSL.getShorthandle(), 8));

        // fourth operand
        var shift = map.get(OPCODEB) * 16;
        operands.add(h.newImmediate(SHIFT, shift, wd));

        return operands;
    }

    // BITFIELD (C4-256)
    private static List<Operand> bitfield(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var wd = fielddata.getBitWidth();

        // first and second operands
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));

        // 3rd and 4th
        operands.add(h.newImmediate(IMMR, 8));
        operands.add(h.newImmediate(IMMS, 8));

        return operands;
    }

    // EXTRACT (C4-256)
    private static List<Operand> extract(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var wd = fielddata.getBitWidth();

        // first, second and third operands
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));
        operands.add(h.newReadRegister(RM, wd));

        // fourth operand
        operands.add(h.newImmediate(IMMS, 8)); // actually 6 bits

        return operands;
    }

    // CONDITIONALBRANCH (C4-257)
    private static List<Operand> conditionalbranch(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        // fetch result of previous operation from "previousInstructionOperands"
        var op = previousInstructionOperands.get(0);
        // first "operand" should be the result register

        operands.add(h.newReadRegister((ArmAsmField) op.getAsmField(),
                op.getValue(), op.getProperties().getWidth()));

        // first operand
        long addr = fielddata.getAddr().longValue();
        Number fullimm = addr + signExtend64(fielddata.getMap().get(IMM) << 2, 21);
        operands.add(h.newImmediateLabel(IMM, fullimm, 64));

        return operands;
    }

    // EXCEPTION (C4-258)
    private static List<Operand> exception(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        operands.add(h.newImmediate(IMM, 16));

        return operands;
    }

    // UNCONDITIONAL BRANCH (REGISTER) (C4-262 to C4-264)
    private static List<Operand> unconditionalbranchreg(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var map = fielddata.getMap();

        // first operand
        operands.add(h.newReadRegister(RN, 64));

        // for brab and braa
        var opa = map.get(OPCODEA); // contains bit "Z"
        if ((opa & 0x0001) != 0) {
            operands.add(h.newReadRegister(RM, map.get(OPCODED), 64));
        }

        return operands;
    }

    // UNCONDITIONAL BRANCH (IMMEDIATE) (C4-264 to C4-265)
    private static List<Operand> unconditionalbranchimm(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        long addr = fielddata.getAddr().longValue();
        Number fullimm = addr + signExtend64(fielddata.getMap().get(IMM) << 2, 28);
        operands.add(h.newImmediateLabel(IMM, fullimm, 64));

        return operands;
    }

    // COMPARE AND BRANCH (C4-265)
    private static List<Operand> compareandbranchimm(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        // first operand
        operands.add(h.newWriteRegister(RT, fielddata.getBitWidth()));

        // second operand
        long addr = fielddata.getAddr().longValue();
        Number fullimm = addr + signExtend64(fielddata.getMap().get(IMM) << 2, 21);
        operands.add(h.newImmediateLabel(IMM, fullimm, 64));

        return operands;
    }

    // TEST_AND_BRANCH (C4-265)
    private static List<Operand> testandbranch(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var map = fielddata.getMap();

        // first operand
        operands.add(h.newReadRegister(RT, fielddata.getBitWidth()));

        // second operand
        var b5 = map.get(SF);
        var b40 = map.get(RM);
        operands.add(h.newImmediate(RM, ((b5 << 5) | b40), 8));

        // third operand
        long addr = fielddata.getAddr().longValue();
        Number label = addr + signExtend64(map.get(IMM) << 2, 16);
        operands.add(h.newImmediateLabel(IMM, label, 64));

        return operands;
    }

    // LOAD_REG_LITERAL (C4-280)
    private static List<Operand> loadregliteralfmt1(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var map = fielddata.getMap();
        var key = makeKey(fielddata);

        // first operand
        operands.add(h.newRegister(key, RT, fielddata.getBitWidth()));

        // second operand
        long addr = fielddata.getAddr().longValue();
        Number label = addr + signExtend64(map.get(IMM) << 2, 21);
        operands.add(h.newImmediateLabel(IMM, label, 64));

        return operands;
    }

    // LOAD_REG_LITERAL (C4-280)
    private static List<Operand> loadregliteralfmt2(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var map = fielddata.getMap();

        // first operand
        operands.add(h.newWriteRegister(RT, fielddata.getBitWidth()));

        // second operand
        long addr = fielddata.getAddr().longValue();
        Number label = addr + signExtend64(map.get(IMM) << 2, 21);
        operands.add(h.newImmediateLabel(IMM, label, 64));

        return operands;
    }

    // LOAD_STORE_PAIR_NO_ALLOC (C4-280 to C4-281)
    // LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1 (C4-281 to C4-282)
    private static List<Operand> loadstore1(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var map = fielddata.getMap();
        var wd = fielddata.getBitWidth();
        var key = makeKey(fielddata);

        // first and second operands
        operands.add(h.newRegister(key, RT, wd));
        operands.add(h.newRegister(key, RM, wd));

        // third operand
        operands.add(h.newReadRegister(RN, 64));

        // fourth (optional) operand
        var imm = map.get(IMM) * (wd / 8);
        Number fullimm = signExtend64(imm, 7);

        if (fielddata.getType() == ArmAsmFieldType.LOAD_STORE_PAIR_NO_ALLOC)
            wd = (wd == 32) ? 8 : 16;
        else // if LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1
            wd = 16;

        operands.add(h.newImmediate(IMM, fullimm, wd));

        return operands;
    }

    // LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2 (C4-282)
    private static List<Operand> loadstore2(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, second, and third operands
        operands.add(h.newReadRegister(RT, 64));
        operands.add(h.newReadRegister(RM, 64));
        operands.add(h.newReadRegister(RN, 64));

        // fourth operand
        operands.add(h.newImmediate(IMM, 16));

        return operands;
    }

    // LOAD_STORE_PAIR_IMM_FMT1
    // LOAD_STORE_PAIR_IMM_FMT2
    // LOAD_STORE_PAIR_IMM_FMT3
    // LOAD_STORE_IMM_PREPOST_FMT1
    // LOAD_STORE_IMM_PREPOST_FMT2
    // LOAD_STORE_IMM_PREPOST_FMT3
    // LOAD_STORE_REG_UIMM_FMT1
    // LOAD_STORE_REG_UIMM_FMT2
    // LOAD_STORE_REG_UIMM_FMT3
    private static List<Operand> loadstore3(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, and second operands
        operands.add(h.newRegister(makeKey(fielddata), RT, fielddata.getBitWidth()));
        operands.add(h.newReadRegister(RN, 64));

        // third operand
        operands.add(h.newImmediate(IMM, 16));

        return operands;
    }

    // LOAD_STORE_REG_OFF_FMT1
    // LOAD_STORE_REG_OFF_FMT2
    // LOAD_STORE_REG_OFF_FMT3
    private static List<Operand> loadstore5(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var wd = fielddata.getBitWidth();

        // first and second operands
        operands.add(h.newRegister(makeKey(fielddata), RT, wd));
        operands.add(h.newReadRegister(RN, 64));

        // third operand
        var wd2 = ((fielddata.getMap().get(OPTION) & 0b001) != 0) ? 64 : 32;
        operands.add(h.newReadRegister(RM, wd2));

        // fourth operand
        String thing = null;
        var option = fielddata.getMap().get(OPTION).intValue();
        if (option == 0b011) {
            thing = ArmInstructionShift.decode(option).getShorthandle();
        } else {
            thing = ArmInstructionExtend.decode(option).getShorthandle();
        }
        operands.add(h.newSubOperation(OPTION, thing, 8));

        // fifth operand
        operands.add(h.newImmediate(S, 8));

        return operands;
    }

    private static List<Operand> dprTwoSource_addSubCarry(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, second, and third operands
        var wd = fielddata.getBitWidth();
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));
        operands.add(h.newReadRegister(RM, wd));
        return operands;
    }

    private static List<Operand> dprOneSource(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, and second operands
        var wd = fielddata.getBitWidth();
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));
        return operands;
    }

    private static List<Operand> shift_ext_reg1(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var wd = fielddata.getBitWidth();

        // first, second, and third operands
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));
        operands.add(h.newReadRegister(RM, wd));

        // fourth operand (sub operation; see page C6-777 of ARMv8 ISA manual)
        var type = fielddata.getType();
        var immval = fielddata.getMap().get(IMM).intValue();

        // if IMM is 0, then there is no extend operation or shift
        if (immval == 0)
            return operands;

        var field = (type != ArmAsmFieldType.ADD_SUB_EXT_REG) ? SHIFT : OPTION;
        var code = fielddata.getMap().get(field).intValue();
        var thing = (field == SHIFT) ? ArmInstructionShift.decode(code).getShorthandle()
                : ArmInstructionExtend.decode(code).getShorthandle();

        operands.add(h.newSubOperation(field, thing, 8));

        // fifth operand (first sub operation operand)
        operands.add(h.newImmediate(IMM, 8));

        return operands;

    }

    private static List<Operand> conditionalCmp(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        var wd = fielddata.getBitWidth();

        // first operand
        operands.add(h.newReadRegister(RN, wd));

        // second operand
        if (fielddata.getType() == ArmAsmFieldType.CONDITIONAL_CMP_REG)
            operands.add(h.newReadRegister(RM, wd));
        else
            operands.add(h.newImmediate(IMM, 8));

        // third operand
        operands.add(h.newImmediate(NZCV, 8));

        // fourth operand
        var cond = fielddata.getMap().get(COND);
        var conds = ArmInstructionCondition.decodeCondition(cond).getShorthandle();
        operands.add(h.newSubOperation(COND, conds, 8));

        return operands;
    }

    private static List<Operand> conditionalSel(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, second, and third operands
        var wd = fielddata.getBitWidth();
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));
        operands.add(h.newReadRegister(RM, wd));

        // fourth operand
        var cond = fielddata.getMap().get(COND);
        var conds = ArmInstructionCondition.decodeCondition(cond).getShorthandle();
        operands.add(h.newSubOperation(COND, conds, 8));

        return operands;
    }

    private static List<Operand> dprThreesource(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        // first, second, third, and fourth operands
        var wd = fielddata.getBitWidth();
        operands.add(h.newWriteRegister(RD, wd));
        operands.add(h.newReadRegister(RN, wd));
        operands.add(h.newReadRegister(RM, wd));
        operands.add(h.newReadRegister(RA, wd));

        return operands;
    }

    private static List<Operand> undefined(ArmAsmFieldData fielddata,
            ArmOperandBuilder h, List<Operand> operands) {

        return operands;
    }
}
