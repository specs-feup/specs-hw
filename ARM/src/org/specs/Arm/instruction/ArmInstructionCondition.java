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
 
package org.specs.Arm.instruction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.InstructionCondition;

/*
 * Predicates for the execution of ARM instructions which contain a "cond" field
 */
public enum ArmInstructionCondition implements InstructionCondition {

    Equal("eq", 0b0000),
    NotEqual("ne", 0b0001),
    CarrySet("cs", 0b0010),

    CarryClear("cc", 0b0011),
    MinusNegative("mn", 0b0100),
    PlusPositiveZero("pl", 0b0101),

    Overflow("vs", 0b0110),
    NoOverflow("vc", 0b0111),
    UnsignedHigher("hi", 0b1000),

    UnsignedLowerSame("ls", 0b1001),
    SignedGreaterThanorEqual("ge", 0b1010),
    SignedLessThan("lt", 0b1011),

    SignedGreaterThan("gt", 0b1100),
    SignedLessThanorEqual("le", 0b1101),
    Always1("al", 0b1110),
    Always2("nvb", 0b1111),

    NONE("NONE", 0b0000);

    private String shorthandle;
    private int condCode;

    private ArmInstructionCondition(String shorthandle, int condCode) {
        this.shorthandle = shorthandle;
        this.condCode = condCode;
    }

    public Boolean isConditional() {
        return this.equals(ArmInstructionCondition.NONE);
    }

    public int getCondCode() {
        return condCode;
    }

    public String getShorthandle() {
        return shorthandle;
    }

    /*
     * maps the condition code to the enum (EXCEPT "NONE"!!!)
     */
    private static final Map<Integer, ArmInstructionCondition> DecodeCond;
    static {
        Map<Integer, ArmInstructionCondition> aMap = new HashMap<Integer, ArmInstructionCondition>();
        for (ArmInstructionCondition cond : ArmInstructionCondition.values())
            if (cond != ArmInstructionCondition.NONE)
                aMap.put(cond.getCondCode(), cond);

        DecodeCond = Collections.unmodifiableMap(aMap);
    }

    public static ArmInstructionCondition decodeCondition(int condcode) {
        return DecodeCond.get(condcode);
    }
}
