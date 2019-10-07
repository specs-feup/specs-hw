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

package org.specs.Arm.isa;

import static org.specs.Arm.isa.ArmInstructionType.*;
import static pt.up.fe.specs.binarytranslation.asmparser.binaryasmparser.BinaryAsmInstructionParser.newInstance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionParser;
import pt.up.fe.specs.binarytranslation.asmparser.IsaParser;
import pt.up.fe.specs.binarytranslation.generic.GenericIsaParser;
import pt.up.fe.specs.util.SpecsSystem;

public interface ArmInstructionParsers {

    List<AsmInstructionParser> PARSERS = Arrays.asList(
            newInstance(DATA_PROCESSING_REGISTER,
                    "cond(4)_000_opcodea(5)_0(8)_imm(5)_opcodeb(2)_00000",
                    data -> !data.get(ArmInstructionFields.COND).equals("1110")),

            newInstance(DATA_PROCESSING_IMMEDIATE,
                    "cond(4)_001_opcodea(5)_registern(4)_imm(16)",
                    data -> !data.get(ArmInstructionFields.COND).equals("1110")),

            // newInstance(MISCELLANEOUS_1,
            // "cond(4)_000_10xx_0_x(15)_0_x(4)",
            // data -> !data.get(ArmInstructionFields.COND).equals("1111")),

            newInstance(UNDEFINED, "x(32)")

    );

    static IsaParser getArmIsaParser() {
        Set<String> allowedFields = new HashSet<>(
                SpecsSystem.getStaticFields(ArmInstructionFields.class, String.class));
        return new GenericIsaParser(PARSERS, allowedFields);
    }
}
