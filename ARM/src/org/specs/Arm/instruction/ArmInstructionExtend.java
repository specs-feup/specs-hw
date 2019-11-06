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

package org.specs.Arm.instruction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ArmInstructionExtend {

    UXTB,
    UXTH,
    UXTW,
    UXTX,
    SXTB,
    SXTH,
    SXTW,
    SXTX;

    private String shorthandle;

    // TODO add a binaryoperator here to implement the shift?

    private ArmInstructionExtend() {
        this.shorthandle = name().toLowerCase();
    }

    public String getShorthandle() {
        return shorthandle;
    }

    /*
     * implements the "DecodeRegExtend" pseudo-code in page 7388 of the armv8 instruction manual:
     * https://static.docs.arm.com/ddi0487/ea/DDI0487E_a_armv8_arm.pdf
     */
    private static final Map<Integer, ArmInstructionExtend> DecodeExtend;
    static {
        Map<Integer, ArmInstructionExtend> aMap = new HashMap<Integer, ArmInstructionExtend>();
        int i = 0;
        for (ArmInstructionExtend ext : ArmInstructionExtend.values())
            aMap.put(i++, ext);
        DecodeExtend = Collections.unmodifiableMap(aMap);
    }

    public static ArmInstructionExtend decode(int extcode) {
        return DecodeExtend.get(extcode);
    }
}
