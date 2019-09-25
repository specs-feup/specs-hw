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
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Generic implementation of interface instruction.
 * 
 * @author JoaoBispo
 *
 */
public class GenericInstruction implements Instruction {

    private final Number address;
    private final String instruction;

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
    public boolean isBackwardsBranch() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAdd() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSub() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isLogical() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isUnaryLogical() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isConditionalJump() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isUnconditionalJump() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isStore() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isLoad() {
        // TODO Auto-generated method stub
        return false;
    }
}
