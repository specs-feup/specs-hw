/**
 * Copyright 2015 SPeCS.
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

package org.specs.MicroBlaze.legacy;

import static org.specs.MicroBlaze.legacy.MbInstructionName.*;

import java.util.HashMap;
import java.util.Map;

public class MbEncoder {

    private final Map<MbInstructionName, AsmEncoder> encoders;

    public MbEncoder() {
        this.encoders = buildMap();
    }

    private static Map<MbInstructionName, AsmEncoder> buildMap() {
        Map<MbInstructionName, AsmEncoder> encoders = new HashMap<>();

        encoders.put(add, new TypeAEncoder("00000000000"));
        encoders.put(addc, new TypeAEncoder("00000000000"));
        encoders.put(addk, new TypeAEncoder("00000000000"));
        encoders.put(addkc, new TypeAEncoder("00000000000"));

        return encoders;
    }

    public int getValue(MbInstruction instruction) {
        return getEncoder(instruction.getInstructionName()).getValue(instruction);
    }

    public String getBinaryString(MbInstruction instruction) {
        return getEncoder(instruction.getInstructionName()).getBinaryString(instruction);
    }

    private AsmEncoder getEncoder(MbInstructionName instructionName) {
        AsmEncoder encoder = encoders.get(instructionName);
        if (encoder == null) {
            throw new RuntimeException("No encoder defined for instruction '" + instructionName + "'");
        }

        return encoder;
    }

}
