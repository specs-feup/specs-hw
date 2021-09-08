package org.specs.Riscv.parsing.getters;

import static org.specs.Riscv.instruction.RiscvOperand.*;
import static org.specs.Riscv.parsing.RiscvAsmField.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.Riscv.parsing.RiscvAsmFieldData;
import org.specs.Riscv.parsing.RiscvAsmFieldType;

import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class RiscvAsmOperandGetter {

    /*
     * map TYPE to a specific private branch target getter func
     */
    interface RiscvAsmOperandParse {
        List<Operand> apply(RiscvAsmFieldData fielddata);
    }

    private static final Map<RiscvAsmFieldType, RiscvAsmOperandParse> TARGETGET;
    static {
        var amap = new HashMap<RiscvAsmFieldType, RiscvAsmOperandParse>();
        amap.put(RiscvAsmFieldType.OP, RiscvAsmOperandGetter::rtype);
        amap.put(RiscvAsmFieldType.AMO, RiscvAsmOperandGetter::rtype);
        amap.put(RiscvAsmFieldType.OPFPa, RiscvAsmOperandGetter::rtype);
        amap.put(RiscvAsmFieldType.OPIMM, RiscvAsmOperandGetter::itype);
        amap.put(RiscvAsmFieldType.LOAD, RiscvAsmOperandGetter::itype);
        amap.put(RiscvAsmFieldType.JALR, RiscvAsmOperandGetter::itype);
        amap.put(RiscvAsmFieldType.STOREFP, RiscvAsmOperandGetter::stype);
        amap.put(RiscvAsmFieldType.LOADFP, RiscvAsmOperandGetter::stype);
        amap.put(RiscvAsmFieldType.STORE, RiscvAsmOperandGetter::stype);
        amap.put(RiscvAsmFieldType.LUI, RiscvAsmOperandGetter::utype);
        amap.put(RiscvAsmFieldType.AUIPC, RiscvAsmOperandGetter::utype);
        amap.put(RiscvAsmFieldType.JAL, RiscvAsmOperandGetter::ujtype);
        amap.put(RiscvAsmFieldType.BRANCH, RiscvAsmOperandGetter::sbtype);

        TARGETGET = Collections.unmodifiableMap(amap);
    }

    public static List<Operand> getFrom(RiscvAsmFieldData fielddata) {

        var func = TARGETGET.get(fielddata.get(RiscvAsmFieldData.TYPE));
        if (func == null)
            func = RiscvAsmOperandGetter::undefined;

        return func.apply(fielddata);
    }

    ///////////////////////////////////////////////////////////////////////
    // R types
    // case OP:
    // case AMO:
    // case OPFPa:
    private static List<Operand> rtype(RiscvAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newWriteRegister(RD, map.get(RD)));
        ops.add(newReadRegister(RS1, map.get(RS1)));
        ops.add(newReadRegister(RS2, map.get(RS2)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // I types
    // case OPIMM:
    // case LOAD:
    // case JALR:
    private static List<Operand> itype(RiscvAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newWriteRegister(RD, map.get(RD)));
        ops.add(newReadRegister(RS1, map.get(RS1)));
        ops.add(newImmediate(IMMTWELVE, map.get(IMMTWELVE)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // S types
    // case STOREFP:
    // case LOADFP:
    // case STORE:
    private static List<Operand> stype(RiscvAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();

        ops.add(newReadRegister(RS1, map.get(RS1)));
        ops.add(newReadRegister(RS2, map.get(RS2)));

        // build full imm field from 2 fields
        var fullimm = (map.get(IMMSEVEN) << 5) | map.get(IMMFIVE);
        ops.add(newImmediate(IMM, fullimm));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // U types
    // case LUI:
    // case AUIPC:
    private static List<Operand> utype(RiscvAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();
        ops.add(newWriteRegister(RD, map.get(RD)));
        ops.add(newImmediate(IMMTWENTY, map.get(IMMTWENTY)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // UJ types
    // case JAL:
    private static List<Operand> ujtype(RiscvAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();

        ops.add(newWriteRegister(RD, map.get(RD)));

        // TODO: this code repeats in RiscvAsmBranchTargetGetter

        // build full imm field from 4 fields
        var bit20 = map.get(BIT20);
        var imm10 = map.get(IMMTEN);
        var bit11 = map.get(BIT11);
        var imm8 = map.get(IMMEIGHT);

        // final addr is this instructions own addr + sign extended fulimm
        var target = BinaryTranslationUtils.signExtend32(
                (bit20 << 20) | (imm8 << 12) | (bit11 << 11) | (imm10 << 1), 21);

        // final addr is this instructions own addr + sign extended fulimm
        var fullimm = fielddata.getAddr().intValue() + target;
        ops.add(newImmediate(IMM, fullimm));

        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // SB-type
    // case BRANCH:
    private static List<Operand> sbtype(RiscvAsmFieldData fielddata) {
        var ops = new ArrayList<Operand>();
        var map = fielddata.getMap();

        ops.add(newReadRegister(RS1, map.get(RS1)));
        ops.add(newReadRegister(RS2, map.get(RS2)));

        // build full imm field from 4 fields
        var bit12 = map.get(BIT12);
        var bit11 = map.get(BIT11);
        var imm6 = map.get(IMMSIX);
        var imm4 = map.get(IMMFOUR);
        var target = BinaryTranslationUtils.signExtend32(
                (bit12 << 12) | (bit11 << 11) | (imm6 << 5) | (imm4 << 1), 12);

        ops.add(newImmediate(IMM, target));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    private static List<Operand> undefined(RiscvAsmFieldData fielddata) {
        return null;
    }
}
