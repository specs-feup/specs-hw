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

package org.specs.Arm.asmparser;

import static org.specs.Arm.asmparser.ArmAsmInstructionType.*;
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
            newInstance(DATA_PROCESSING_IMMEDIATE_SHIFT,
                    "cond(4)_000_opcode(4)_S(1)_Rn(4)_Rd(4)_shift_amount(5)_shift(2)_0_Rm(4)",
                    data -> !data.get(ArmAsmFields.COND).equals("1111")),

            newInstance(MISCELLANEOUS_1,
                    "cond(4)_000_10xx_0_x(15)_0_x(4)",
                    data -> !data.get(ArmAsmFields.COND).equals("1111")),

            newInstance(UNDEFINED, "x(32)")

    );

    static IsaParser getArmIsaParser() {
        Set<String> allowedFields = new HashSet<>(SpecsSystem.getStaticFields(ArmAsmFields.class, String.class));
        return new GenericIsaParser(PARSERS, allowedFields);
    }
}
