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
 
package org.specs.Arm.parsing;

import static org.specs.Arm.parsing.ArmAsmField.IMM;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class ArmAsmBranchTargetGetter {

    /*
     * map TYPE to a specific private branch target getter func
     */
    interface ArmAsmBranchParse {
        Number apply(ArmAsmFieldData fielddata);
    }

    private static final Map<ArmAsmFieldType, ArmAsmBranchParse> TARGETGET;
    static {
        Map<ArmAsmFieldType, ArmAsmBranchParse> amap = new HashMap<ArmAsmFieldType, ArmAsmBranchParse>();
        amap.put(ArmAsmFieldType.CONDITIONALBRANCH, ArmAsmBranchTargetGetter::conditionalbranch);
        amap.put(ArmAsmFieldType.UCONDITIONALBRANCH_REG, ArmAsmBranchTargetGetter::unconditionalbranchreg);
        amap.put(ArmAsmFieldType.UCONDITIONALBRANCH_IMM, ArmAsmBranchTargetGetter::unconditionalbranchimm);
        amap.put(ArmAsmFieldType.COMPARE_AND_BRANCH_IMM, ArmAsmBranchTargetGetter::compareandbranchimm);
        amap.put(ArmAsmFieldType.TEST_AND_BRANCH, ArmAsmBranchTargetGetter::testandbranch);
        amap.put(ArmAsmFieldType.UNDEFINED, ArmAsmBranchTargetGetter::undefined);

        TARGETGET = Collections.unmodifiableMap(amap);
    }

    public static Number getFrom(ArmAsmFieldData fielddata) {

        var func = TARGETGET.get(fielddata.get(ArmAsmFieldData.TYPE));
        if (func == null)
            func = ArmAsmBranchTargetGetter::undefined;

        return func.apply(fielddata);
    }

    private static Number conditionalbranch(ArmAsmFieldData fielddata) {
        var map = fielddata.getMap();
        return BinaryTranslationUtils.signExtend32(map.get(IMM) << 2, 21);
    }

    private static Number unconditionalbranchreg(ArmAsmFieldData fielddata) {
        // TODO the return in this case is the value held in a register... how to treat??
        return 0;
    }

    private static Number unconditionalbranchimm(ArmAsmFieldData fielddata) {
        var map = fielddata.getMap();
        return BinaryTranslationUtils.signExtend64(map.get(IMM) << 2, 28);
    }

    private static Number compareandbranchimm(ArmAsmFieldData fielddata) {
        var map = fielddata.getMap();
        return BinaryTranslationUtils.signExtend64(map.get(IMM) << 2, 21);
    }

    private static Number testandbranch(ArmAsmFieldData fielddata) {
        var map = fielddata.getMap();
        return BinaryTranslationUtils.signExtend64(map.get(IMM) << 2, 16);
    }

    private static Number undefined(ArmAsmFieldData fielddata) {
        return 0;
    }
}
