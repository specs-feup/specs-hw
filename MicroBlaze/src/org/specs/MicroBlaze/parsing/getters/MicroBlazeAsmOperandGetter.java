package org.specs.MicroBlaze.parsing.getters;

import static org.specs.MicroBlaze.instruction.MicroBlazeOperand.*;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.MicroBlaze.instruction.MicroBlazeRegisterDump;
import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldData;
import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldType;
import org.specs.MicroBlaze.parsing.MicroBlazeRegisterResolver;

import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.instruction.register.ExecutedImmediate;

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
        List<Operand> apply(MicroBlazeRegisterResolver resolver);
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

    public static List<Operand> getFrom(MicroBlazeAsmFieldData fielddata, MicroBlazeRegisterDump registers) {

        var func = TARGETGET.get(fielddata.get(MicroBlazeAsmFieldData.TYPE));
        if (func == null)
            func = MicroBlazeAsmOperandGetter::undefined;

        var resolver = new MicroBlazeRegisterResolver(fielddata, registers, postedImm, upper16Imm);
        return func.apply(resolver);
    }

    ///////////////////////////////////////////////////////////////////////
    // MBAR
    private static List<Operand> mbar(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newImmediate(resolver.resolveIMM()));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // ULBRANCH
    private static List<Operand> ulbranch(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newWriteRegister(resolver.resolve(RD)));
        ops.add(newReadRegister(resolver.resolve(RB)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // UBRANCH
    private static List<Operand> ubranch(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newReadRegister(resolver.resolve(RB)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // UILBRANCH
    private static List<Operand> uilbranch(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newWriteRegister(resolver.resolve(RD)));
        ops.add(newImmediate(resolver.resolveFullIMM()));

        // TODO: correct? what if branch is relative/absolute?
        // bri brai etc

        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // UIBRANCH
    private static List<Operand> uibranch(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newImmediate(resolver.resolveFullIMM()));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // CIBRANCH:
    // RETURN:
    private static List<Operand> cibranchAndRet(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newWriteRegister(resolver.resolve(RA)));
        ops.add(newImmediate(resolver.resolveFullIMM()));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // CBRANCH:
    private static List<Operand> cbranch(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newWriteRegister(resolver.resolve(RA)));
        ops.add(newWriteRegister(resolver.resolve(RB)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // IBARREL_FMT1:
    private static List<Operand> ibarrel1(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newWriteRegister(resolver.resolve(RD)));
        ops.add(newWriteRegister(resolver.resolve(RA)));
        ops.add(newImmediate(resolver.resolveFullIMM()));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // IBARREL_FMT2:
    private static List<Operand> ibarrel2(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();

        ops.add(newWriteRegister(resolver.resolve(RD)));
        ops.add(newReadRegister(resolver.resolve(RA)));
        ops.add(newImmediate(resolver.resolveIMM())); // TODO: needs full IMM?

        // unique case...
        var val = resolver.getFielddata().getMap().get(IMMW);
        ops.add(newImmediate(new ExecutedImmediate(IMMW, val)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // STREAM:
    private static List<Operand> stream(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newWriteRegister(resolver.resolve(RD)));
        ops.add(newReadRegister(resolver.resolve(RA)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // DSTREAM:
    private static List<Operand> dstream(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newReadRegister(resolver.resolve(RA)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // IMM:
    private static List<Operand> imm(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        var imm = resolver.resolveIMM();
        ops.add(newImmediate(imm));
        upper16Imm = (imm.getDataValue().intValue()) << 16;
        postedImm = true;
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // TYPE_A_STORE:
    private static List<Operand> typeAStore(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newReadRegister(resolver.resolve(RD)));
        ops.add(newReadRegister(resolver.resolve(RA)));
        ops.add(newReadRegister(resolver.resolve(RB)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // TYPE_A:
    private static List<Operand> typeA(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newWriteRegister(resolver.resolve(RD)));
        ops.add(newReadRegister(resolver.resolve(RA)));
        ops.add(newReadRegister(resolver.resolve(RB)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // TYPE_B_STORE:
    private static List<Operand> typeBStore(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newReadRegister(resolver.resolve(RD)));
        ops.add(newReadRegister(resolver.resolve(RA)));
        ops.add(newImmediate(resolver.resolveFullIMM()));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // TYPE_B:
    private static List<Operand> typeB(MicroBlazeRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newWriteRegister(resolver.resolve(RD)));
        ops.add(newReadRegister(resolver.resolve(RA)));
        ops.add(newImmediate(resolver.resolveFullIMM()));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    private static List<Operand> undefined(MicroBlazeRegisterResolver resolver) {
        return null;
    }
}
