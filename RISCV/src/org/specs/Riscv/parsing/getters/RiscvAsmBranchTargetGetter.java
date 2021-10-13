/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
package org.specs.Riscv.parsing.getters;

import static org.specs.Riscv.parsing.RiscvAsmField.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.specs.Riscv.parsing.RiscvAsmFieldData;
import org.specs.Riscv.parsing.RiscvAsmFieldType;

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
        var amap = new HashMap<RiscvAsmFieldType, RiscvAsmBranchParse>();
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
        var imm12 = map.get(IMMTWELVE);
        var target = BinaryTranslationUtils.signExtend32(imm12, 12);
        var rs1 = map.get(RS1);

        // var rs1Value = ??? inst.getRegister... etc ??

        return Long.valueOf(rs1 + target);
        // TODO: correct?? no, I need the VALUE of rs1, not the register name
    }

    private static Number uj(RiscvAsmFieldData fielddata) {

        // build full imm field from 4 fields
        var map = fielddata.getMap();
        var bit20 = map.get(BIT20);
        var imm10 = map.get(IMMTEN);
        var bit11 = map.get(BIT11);
        var imm8 = map.get(IMMEIGHT);

        // final addr is this instructions own addr + sign extended fulimm
        var target = BinaryTranslationUtils.signExtend32(
                (bit20 << 20) | (imm8 << 12) | (bit11 << 11) | (imm10 << 1), 21);

        return Long.valueOf(fielddata.getAddr().longValue() + target);
        /*
        return BinaryTranslationUtils.signExtend32(
                (bit20 << 20) | (imm8 << 12) | (bit11 << 11) | (imm10 << 1), 21);*/
    }

    private static Number sb(RiscvAsmFieldData fielddata) {

        // build full imm field from 4 fields
        var map = fielddata.getMap();
        var bit12 = map.get(BIT12);
        var bit11 = map.get(BIT11);
        var imm6 = map.get(IMMSIX);
        var imm4 = map.get(IMMFOUR);
        var target = BinaryTranslationUtils.signExtend32(
                (bit12 << 12) | (bit11 << 11) | (imm6 << 5) | (imm4 << 1), 12);

        return Long.valueOf(target);
    }

    private static Number undefined(RiscvAsmFieldData fielddata) {
        return 0;
    }
}
