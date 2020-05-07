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

import static org.specs.Arm.parsing.ArmAsmField.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        amap.put(ArmAsmFieldType.DPI_ADDSUBIMM, ArmAsmWidthGetter::getter1);
        amap.put(ArmAsmFieldType.LOGICAL, ArmAsmWidthGetter::getter1);
        amap.put(ArmAsmFieldType.MOVEW, ArmAsmWidthGetter::getter1);
        amap.put(ArmAsmFieldType.EXTRACT, ArmAsmWidthGetter::getter1);
        amap.put(ArmAsmFieldType.BITFIELD, ArmAsmWidthGetter::getter1);
        amap.put(ArmAsmFieldType.DPR_TWOSOURCE, ArmAsmWidthGetter::getter1);
        amap.put(ArmAsmFieldType.LOGICAL_SHIFT_REG, ArmAsmWidthGetter::getter1);
        amap.put(ArmAsmFieldType.ADD_SUB_SHIFT_REG, ArmAsmWidthGetter::getter1);
        amap.put(ArmAsmFieldType.ADD_SUB_EXT_REG, ArmAsmWidthGetter::getter1);
        amap.put(ArmAsmFieldType.ADD_SUB_CARRY, ArmAsmWidthGetter::getter1);

        amap.put(ArmAsmFieldType.LOAD_REG_LITERAL_FMT1, ArmAsmWidthGetter::getter2b);
        amap.put(ArmAsmFieldType.LOAD_REG_LITERAL_FMT2, ArmAsmWidthGetter::getter1b);
        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_NO_ALLOC, ArmAsmWidthGetter::getter2a);

        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1, ArmAsmWidthGetter::getter2a);
        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2, ArmAsmWidthGetter::getter1);

        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT1, ArmAsmWidthGetter::getter1b);
        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT2, ArmAsmWidthGetter::getter3b);
        amap.put(ArmAsmFieldType.LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT3, ArmAsmWidthGetter::getter4);

        amap.put(ArmAsmFieldType.LOAD_STORE_REG_IMM_PREPOST_FMT1, ArmAsmWidthGetter::getter1b);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_IMM_PREPOST_FMT2, ArmAsmWidthGetter::getter3);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_IMM_PREPOST_FMT3, ArmAsmWidthGetter::getter4);

        amap.put(ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT1, ArmAsmWidthGetter::getter1b);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT2, ArmAsmWidthGetter::getter3);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT3, ArmAsmWidthGetter::getter4);

        amap.put(ArmAsmFieldType.LOAD_STORE_REG_UIMM_FMT1, ArmAsmWidthGetter::getter1b);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_UIMM_FMT2, ArmAsmWidthGetter::getter3);
        amap.put(ArmAsmFieldType.LOAD_STORE_REG_UIMM_FMT3, ArmAsmWidthGetter::getter4);

        amap.put(ArmAsmFieldType.FP_DPR_ONESOURCE, ArmAsmWidthGetter::getter5);
        amap.put(ArmAsmFieldType.FP_COMPARE, ArmAsmWidthGetter::getter5);
        amap.put(ArmAsmFieldType.FP_IMMEDIATE, ArmAsmWidthGetter::getter5);
        amap.put(ArmAsmFieldType.FP_COND_COMPARE, ArmAsmWidthGetter::getter5);
        amap.put(ArmAsmFieldType.FP_DPR_TWOSOURCE, ArmAsmWidthGetter::getter5);
        amap.put(ArmAsmFieldType.FP_COND_SELECT, ArmAsmWidthGetter::getter5);
        amap.put(ArmAsmFieldType.FP_DPR_THREESOURCE, ArmAsmWidthGetter::getter5);

        WIDTHGET = Collections.unmodifiableMap(amap);
    }

    public static int getFrom(ArmAsmFieldData fielddata) {
        var map = fielddata.getMap();
        var func = WIDTHGET.get(fielddata.get(ArmAsmFieldData.TYPE));
        if (func != null)
            return func.apply(map);
        else
            return 32; // default value
    }

    // sf is one bit
    private static int getter1(Map<ArmAsmField, Integer> map) {
        return (map.get(SF) != 0) ? 64 : 32;
    }

    // LOAD_REG_LITERAL_FMT2
    private static int getter1b(Map<ArmAsmField, Integer> map) {
        return (map.get(OPCODEA) != 0) ? 64 : 32;
    }

    // sf is two bits
    // LOAD_STORE_PAIR_NO_ALLOC
    private static int getter2a(Map<ArmAsmField, Integer> map) {

        var simd = map.get(SIMD) != 0;
        var sf = map.get(SF);
        if (!simd)
            sf = sf >> 1;

        return ((int) Math.pow(2, sf)) * 32;
    }

    // sf is two bits
    private static int getter2b(Map<ArmAsmField, Integer> map) {
        return ((int) Math.pow(2, map.get(SF))) * 32;
    }

    // fields "opcodea" and "opcodeb" used for size
    private static int getter3(Map<ArmAsmField, Integer> map) {
        Boolean a, b, c, d;
        a = (map.get(OPCODEA) & 0b10) != 0;
        b = (map.get(OPCODEA) & 0b01) != 0;
        c = (map.get(OPCODEB) & 0b10) != 0;
        d = (map.get(OPCODEB) & 0b01) != 0;
        var sf = ((!a & !d) | (b & !c)) ? 64 : 32;
        return sf;
    }

    // LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT2
    private static int getter3b(Map<ArmAsmField, Integer> map) {
        Boolean a, b, c, d;
        a = (map.get(OPCODEA) & 0b10) != 0;
        b = (map.get(OPCODEA) & 0b01) != 0;
        c = (map.get(OPCODEB) & 0b10) != 0;
        d = (map.get(OPCODEB) & 0b01) != 0;
        var sf = ((!a & !d) | (b & !c)) ? 64 : 32;
        return sf;
    }

    // two fields, sfa, and sfb
    private static int getter4(Map<ArmAsmField, Integer> map) {
        var sf = (map.get(SFB) << 2) | map.get(SFA);
        return ((int) Math.pow(2, sf)) * 8;
    }

    // FP_DPR_ONESOURCE (C4-352)
    // one field: ptype
    private static int getter5(Map<ArmAsmField, Integer> map) {
        var ptype = (map.get(PTYPE));
        var bitwidth = 0;
        switch (ptype) {
        case 0:
            bitwidth = 32;
            break;
        case 1:
            bitwidth = 64;
            break;
        case 2:
            bitwidth = 16;
            break;
        default:
            bitwidth = 32;
        }
        return bitwidth;
    }
}
