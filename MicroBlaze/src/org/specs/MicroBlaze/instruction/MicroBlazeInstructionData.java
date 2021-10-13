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
 
package org.specs.MicroBlaze.instruction;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldData;

import pt.up.fe.specs.binarytranslation.instruction.AInstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;

public class MicroBlazeInstructionData extends AInstructionData {

    /*
     * Only public constructor
     */
    public MicroBlazeInstructionData(
            InstructionProperties props,
            MicroBlazeAsmFieldData fieldData,
            MicroBlazeRegisterDump registers) {
        super(props, fieldData, registers);
    }

    /*
     * Helper constructor for copy, calls super copy
     */
    private MicroBlazeInstructionData(MicroBlazeInstructionData other) {
        super(other);
    }

    /*
     * Copy "constructor"
     */
    @Override
    public MicroBlazeInstructionData copy() {
        return new MicroBlazeInstructionData(this);
    }
}
