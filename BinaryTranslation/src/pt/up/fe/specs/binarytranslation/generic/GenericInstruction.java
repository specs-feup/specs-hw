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

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.InstructionType;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Generic implementation of interface instruction.
 * 
 * @author JoaoBispo
 *
 */
public abstract class GenericInstruction implements Instruction {

    private Number address;
    protected int latency;
    private String instruction;
    protected InstructionType genericType;

    public GenericInstruction(Number address, String instruction) {
        this.address = address;
        this.instruction = instruction;
    }

    @Override
    public int getLatency() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Number getAddress() {
        return address;
    }

    @Override
    public String getInstruction() {
        return instruction;
    }

    @Override
    public String toString() {
        return SpecsStrings.toHexString(address.longValue(), 8) + ": " + instruction;
    }

    @Override
    public boolean isForwardsBranch() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isBackwardsBranch() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAdd() {
        return this.genericType == InstructionType.add;
    }

    @Override
    public boolean isSub() {
        return this.genericType == InstructionType.sub;
    }

    @Override
    public boolean isLogical() {
        return this.genericType == InstructionType.logical;
    }

    @Override
    public boolean isUnaryLogical() {
        return this.genericType == InstructionType.unarylogical;
    }

    @Override
    public boolean isConditionalJump() {
        return this.genericType == InstructionType.cjump;
    }

    @Override
    public boolean isUnconditionalJump() {
        return this.genericType == InstructionType.ujump;
    }

    @Override
    public boolean isStore() {
        return this.genericType == InstructionType.store;
    }

    @Override
    public boolean isLoad() {
        return this.genericType == InstructionType.load;
    }
}
