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
 
package org.specs.Riscv.instruction;

import org.specs.Riscv.parsing.RiscvAsmFieldData;

import pt.up.fe.specs.binarytranslation.instruction.AInstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;

public class RiscvInstructionData extends AInstructionData {

    /*
     * Only public constructor
     */
    public RiscvInstructionData(
            InstructionProperties props,
            RiscvAsmFieldData fieldData,
            RiscvRegisterDump registers) {
        super(props, fieldData, registers);
        // TODO: some of the code in getOperands and getBranchTarget for RISCV is repeated
        // and could easily break :(
    }

    /*
     * Helper constructor for copy, calls super copy
     */
    private RiscvInstructionData(RiscvInstructionData other) {
        super(other);
    }

    /*
     * Copy "constructor"
     */
    @Override
    public RiscvInstructionData copy() {
        return new RiscvInstructionData(this);
    }
}
