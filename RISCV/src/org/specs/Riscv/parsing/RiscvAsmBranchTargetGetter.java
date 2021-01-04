package org.specs.Riscv.parsing;

import static org.specs.Riscv.parsing.RiscvAsmField.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class RiscvAsmBranchTargetGetter {

    /*
     * map TYPE to a specific private branch target getter func
     */
    interface RiscvAsmBranchParse {
        Number apply(RiscvAsmFieldData fielddata);
    }

    private static final Map<RiscvAsmFieldType, RiscvAsmBranchParse> TARGETGET;
    static {
        Map<RiscvAsmFieldType, RiscvAsmBranchParse> amap = new HashMap<RiscvAsmFieldType, RiscvAsmBranchParse>();
        amap.put(RiscvAsmFieldType.JALR, RiscvAsmBranchTargetGetter::jalr);
        amap.put(RiscvAsmFieldType.BRANCH, RiscvAsmBranchTargetGetter::sb);
        amap.put(RiscvAsmFieldType.JAL, RiscvAsmBranchTargetGetter::uj);

        TARGETGET = Collections.unmodifiableMap(amap);
    }

    public static Number getFrom(RiscvAsmFieldData fielddata) {

        var func = TARGETGET.get(fielddata.get(RiscvAsmFieldData.TYPE));
        if (func == null)
            func = RiscvAsmBranchTargetGetter::undefined;

        return func.apply(fielddata);
    }

    private static Number jalr(RiscvAsmFieldData fielddata) {
        var map = fielddata.getMap();
        return BinaryTranslationUtils.signExtend32(map.get(IMMTWELVE), 12);
        // TODO: how to get target of relative jumps??
        // this jump IMM is added to the value of rs1...
    }

    private static Number uj(RiscvAsmFieldData fielddata) {

        // build full imm field from 4 fields
        var map = fielddata.getMap();
        var bit20 = map.get(BIT20);
        var imm10 = map.get(IMMTEN);
        var bit11 = map.get(BIT11);
        var imm8 = map.get(IMMEIGHT);
        return BinaryTranslationUtils.signExtend32(
                (bit20 << 20) | (imm8 << 12) | (bit11 << 11) | (imm10 << 1), 21);
    }

    private static Number sb(RiscvAsmFieldData fielddata) {

        // build full imm field from 4 fields
        var map = fielddata.getMap();
        var bit12 = map.get(BIT12);
        var bit11 = map.get(BIT11);
        var imm6 = map.get(IMMSIX);
        var imm4 = map.get(IMMFOUR);
        return BinaryTranslationUtils.signExtend32(
                (bit12 << 12) | (bit11 << 11) | (imm6 << 5) | (imm4 << 1), 12);
    }

    private static Number undefined(RiscvAsmFieldData fielddata) {
        return 0;
    }
}
