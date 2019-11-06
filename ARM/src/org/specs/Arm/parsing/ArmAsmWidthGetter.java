/**
 * Copyright 2019 SPeCS.
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

import java.util.*;

public class ArmAsmWidthGetter {

    /*
     * map TYPE to a specific private operand init function (to avoid switch case)
     */
    interface ArmAsmWidthParse {
        int apply(Map<ArmAsmField, Integer> map);
    }

    private static final Map<ArmAsmFieldType, ArmAsmWidthParse> WIDTHGET;
    static {
        Map<ArmAsmFieldType, ArmAsmWidthParse> amap = new HashMap<ArmAsmFieldType, ArmAsmWidthParse>();
        /*        amap.put(ArmAsmFieldType.DPI_PCREL, ArmAsmOperandGetter::dpi_pcrel);
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
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT1, ArmAsmOperandGetter::loadstore5a);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT2, ArmAsmOperandGetter::loadstore5a);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT3, ArmAsmOperandGetter::loadstore5b);
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
        amap.put(ArmAsmFieldType.UNDEFINED, ArmAsmOperandGetter::undefined);
        */
        amap.put(ArmAsmFieldType.DPI_PCREL, ArmAsmWidthGetter::getter1);

        WIDTHGET = Collections.unmodifiableMap(amap);
    }

    public int getFrom(ArmAsmFieldData fielddata) {
        var map = fielddata.getMap();
        var func = WIDTHGET.get(fielddata.get(ArmAsmFieldData.TYPE));
        if (func != null)
            return func.apply(map);
        return 32;
    }

    private static int getter1(Map<ArmAsmField, Integer> map) {

        return 32;
    }
}
