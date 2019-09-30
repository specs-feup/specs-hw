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

package pt.up.fe.specs.binarytranslation.generic;

import pt.up.fe.specs.util.exceptions.NotImplementedException;

public class GenericInstruction extends AInstruction {

    private final Number instructionCode;

    public GenericInstruction(Number address, Number instruction) {
        super(address, instruction.toString());

        this.instructionCode = instruction;
    }

    @Override
    public Number getInstructionCode() {
        return instructionCode;
    }

    @Override
    public Number getBranchTarget() {
        throw new NotImplementedException(this);
    }

}
