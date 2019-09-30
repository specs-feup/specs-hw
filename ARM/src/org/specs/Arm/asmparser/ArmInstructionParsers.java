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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionParser;
import pt.up.fe.specs.binarytranslation.asmparser.IsaParser;
import pt.up.fe.specs.binarytranslation.asmparser.impl.GenericIsaParser;
import pt.up.fe.specs.util.SpecsSystem;

public interface ArmInstructionParsers {

    static IsaParser getArmIsaParser() {
        List<AsmInstructionParser> instParsers = SpecsSystem.getStaticFields(ArmInstructionParsers.class,
                AsmInstructionParser.class);

        Set<String> allowedFields = new HashSet<>(SpecsSystem.getStaticFields(ArmAsmFields.class, String.class));

        return new GenericIsaParser(instParsers, allowedFields);
    }
}
