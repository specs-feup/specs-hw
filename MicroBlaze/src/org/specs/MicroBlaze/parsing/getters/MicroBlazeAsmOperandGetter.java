package org.specs.MicroBlaze.parsing.getters;

import static org.specs.MicroBlaze.instruction.MicroBlazeOperand.*;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldData;
import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldType;

import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;

public class MicroBlazeAsmOperandGetter {

    /*
     * Helpers for IMM values
     */
    private static boolean postedImm = false;
    private static int upper16Imm = 0;

    /*
     * map TYPE to a specific private branch target getter func
     */
    interface MicroBlazeAsmOperandParse {
        List<Operand> apply(MicroBlazeAsmFieldData fielddata);
    }

    private static final Map<MicroBlazeAsmFieldType, MicroBlazeAsmOperandParse> TARGETGET;
    static {
        var amap = new HashMap<MicroBlazeAsmFieldType, MicroBlazeAsmOperandParse>();
        amap.put(MicroBlazeAsmFieldType.MBAR, MicroBlazeAsmOperandGetter::mbar);
        amap.put(MicroBlazeAsmFieldType.ULBRANCH, MicroBlazeAsmOperandGetter::ulbranch);
        amap.put(MicroBlazeAsmFieldType.UBRANCH, MicroBlazeAsmOperandGetter::ubranch);
        amap.put(MicroBlazeAsmFieldType.UILBRANCH, MicroBlazeAsmOperandGetter::uilbranch);
        amap.put(MicroBlazeAsmFieldType.UIBRANCH, MicroBlazeAsmOperandGetter::uibranch);
        amap.put(MicroBlazeAsmFieldType.CBRANCH, MicroBlazeAsmOperandGetter::cbranch);
        amap.put(MicroBlazeAsmFieldType.CIBRANCH, MicroBlazeAsmOperandGetter::cibranchAndRet);
        amap.put(MicroBlazeAsmFieldType.RETURN, MicroBlazeAsmOperandGetter::cibranchAndRet);
        amap.put(MicroBlazeAsmFieldType.IBARREL_FMT1, MicroBlazeAsmOperandGetter::ibarrel1);
        amap.put(MicroBlazeAsmFieldType.IBARREL_FMT2, MicroBlazeAsmOperandGetter::ibarrel2);
        amap.put(MicroBlazeAsmFieldType.STREAM, MicroBlazeAsmOperandGetter::stream);
        amap.put(MicroBlazeAsmFieldType.DSTREAM, MicroBlazeAsmOperandGetter::dstream);
        amap.put(MicroBlazeAsmFieldType.IMM, MicroBlazeAsmOperandGetter::imm);
        amap.put(MicroBlazeAsmFieldType.TYPE_A_STORE, MicroBlazeAsmOperandGetter::typeAStore);
        amap.put(MicroBlazeAsmFieldType.TYPE_A, MicroBlazeAsmOperandGetter::typeA);
        amap.put(MicroBlazeAsmFieldType.TYPE_B_STORE, MicroBlazeAsmOperandGetter::typeBStore);
        amap.put(MicroBlazeAsmFieldType.TYPE_B, MicroBlazeAsmOperandGetter::typeB);

        TARGETGET = Collections.unmodifiableMap(amap);
    }

    public static List<Operand> getFrom(MicroBlazeAsmFieldData fielddata, RegisterDump registers) {

        var func = TARGETGET.get(fielddata.get(MicroBlazeAsmFieldData.TYPE));
        if (func == null)
            func = MicroBlazeAsmOperandGetter::undefined;

        return func.apply(fielddata);
    }

    private static int getFullIMM(MicroBlazeAsmFieldData fielddata) {

        var map = fielddata.getMap();

        // resolve IMM value first, operation has any
        int fullimm = 0;
        if (map.containsKey(IMM)) {
            var lower16 = map.get(IMM);

            // sign extend if no posted IMM
            if (postedImm == false) {
                fullimm = (lower16 << (16)) >> (16);
            }

            // else combine (assume upper16Imm already shifted up 16 bits)
            else {
                postedImm = false;
                fullimm = upper16Imm | lower16;
            }
        }

        return fullimm;
    }

    public static boolean isPostedImm() {
        return postedImm;
    }

    public static int getUpper16Imm() {
        return upper16Imm;
    }

    ///////////////////////////////////////////////////////////////////////
    // MBAR
    private static List<Operand> mbar(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newImmediate(IMM, map.get(IMM)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // ULBRANCH
    private static List<Operand> ulbranch(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newWriteRegister(RD, map.get(RD)));
        ops.add(newReadRegister(RB, map.get(RB)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // UBRANCH
    private static List<Operand> ubranch(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newReadRegister(RB, map.get(RB)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // UILBRANCH
    private static List<Operand> uilbranch(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newWriteRegister(RD, map.get(RD)));
        ops.add(newImmediate(IMM, getFullIMM(fielddata)));

        // TODO: correct? what if branch is relative/absolute?
        // bri brai etc

        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // UIBRANCH
    private static List<Operand> uibranch(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        ops.add(newImmediate(IMM, getFullIMM(fielddata)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // CIBRANCH:
    // RETURN:
    private static List<Operand> cibranchAndRet(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newReadRegister(RA, map.get(RA)));
        ops.add(newImmediate(IMM, getFullIMM(fielddata)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // CBRANCH:
    private static List<Operand> cbranch(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newReadRegister(RA, map.get(RA)));
        ops.add(newReadRegister(RB, map.get(RB)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // IBARREL_FMT1:
    private static List<Operand> ibarrel1(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newWriteRegister(RD, map.get(RD)));
        ops.add(newReadRegister(RA, map.get(RA)));
        ops.add(newImmediate(IMM, getFullIMM(fielddata)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // IBARREL_FMT2:
    private static List<Operand> ibarrel2(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newWriteRegister(RD, map.get(RD)));
        ops.add(newReadRegister(RA, map.get(RA)));
        ops.add(newImmediate(IMM, map.get(IMM))); // TODO: needs full IMM?
        ops.add(newImmediate(IMMW, map.get(IMMW)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // STREAM:
    private static List<Operand> stream(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newWriteRegister(RD, map.get(RD)));
        ops.add(newReadRegister(RA, map.get(RA)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // DSTREAM:
    private static List<Operand> dstream(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newReadRegister(RA, map.get(RA)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // IMM:
    private static List<Operand> imm(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newImmediate(IMM, map.get(IMM)));
        upper16Imm = (map.get(IMM) << 16);
        postedImm = true;
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // TYPE_A_STORE:
    private static List<Operand> typeAStore(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newReadRegister(RD, map.get(RD)));
        ops.add(newReadRegister(RA, map.get(RA)));
        ops.add(newReadRegister(RB, map.get(RB)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // TYPE_A:
    private static List<Operand> typeA(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newWriteRegister(RD, map.get(RD)));
        ops.add(newReadRegister(RA, map.get(RA)));
        ops.add(newReadRegister(RB, map.get(RB)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // TYPE_B_STORE:
    private static List<Operand> typeBStore(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newReadRegister(RD, map.get(RD)));
        ops.add(newReadRegister(RA, map.get(RA)));
        ops.add(newImmediate(IMM, getFullIMM(fielddata)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // TYPE_B:
    private static List<Operand> typeB(MicroBlazeAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newWriteRegister(RD, map.get(RD)));
        ops.add(newReadRegister(RA, map.get(RA)));
        ops.add(newImmediate(IMM, getFullIMM(fielddata)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    private static List<Operand> undefined(MicroBlazeAsmFieldData fielddata) {
        return null;
    }
}
