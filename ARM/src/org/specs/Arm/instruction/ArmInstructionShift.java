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

public enum ArmInstructionShift {

    LSL,
    LSR,
    ASR,
    ROR;

    private String shorthandle;

    // TODO add a binaryoperator here to implement the shift?

    private ArmInstructionShift() {
        this.shorthandle = name().toLowerCase();
    }

    public String getShorthandle() {
        return shorthandle;
    }

    /*
     * implements the "DecodeShift" pseudo-code in page 7390 of the armv8 instruction manual:
     * https://static.docs.arm.com/ddi0487/ea/DDI0487E_a_armv8_arm.pdf
     */
    private static final Map<Integer, ArmInstructionShift> DecodeShift;
    static {
        Map<Integer, ArmInstructionShift> aMap = new HashMap<Integer, ArmInstructionShift>();

        int i = 0;
        for (ArmInstructionShift shift : ArmInstructionShift.values())
            aMap.put(i++, shift);

        DecodeShift = Collections.unmodifiableMap(aMap);
    }

    public static ArmInstructionShift decode(int shiftcode) {
        return DecodeShift.get(shiftcode);
    }
}
