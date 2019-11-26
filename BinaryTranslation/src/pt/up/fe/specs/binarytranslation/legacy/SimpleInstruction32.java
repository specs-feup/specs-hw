/*
 * Copyright 2011 SPeCS Research Group.
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

package pt.up.fe.specs.binarytranslation.legacy;

import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Represents a simple instruction: an integer for the address and a string for the instruction.
 *
 * @author Joao Bispo
 */
public interface SimpleInstruction32 extends Instruction {

    int getAddress32();

    @Override
    default InstructionData getData() {
        throw new NotImplementedException(this);
    }

    @Override
    default Number getBranchTarget() {
        return 4;
        // TODO fix
    }

    @Override
    default Number getAddress() {
        return getAddress32();
    }

    @Override
    default String getInstruction() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default int getLatency() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    default String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default boolean isAdd() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isBackwardsJump() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isConditionalJump() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isForwardsJump() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isLoad() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isLogical() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isStore() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isSub() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isUnary() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isUnconditionalJump() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isAbsoluteJump() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isRelativeJump() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isFloat() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isImmediate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isJump() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isMemory() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isMul() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isUnknown() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default int getDelay() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    default void printInstruction() {
        // TODO Auto-generated method stub

    }

    @Override
    default AsmFieldData getFieldData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default InstructionProperties getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default String getRepresentation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default Instruction copy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default void makeSymbolic(Number address, Map<String, String> regremap) {
        // TODO Auto-generated method stub

    }

    @Override
    default String express() {
        // TODO Auto-generated method stub
        return null;
    }
}