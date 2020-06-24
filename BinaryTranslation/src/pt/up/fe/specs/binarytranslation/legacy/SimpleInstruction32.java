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
import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
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
        throw new NotImplementedException(this);
    }

    @Override
    default int getLatency() {
        throw new NotImplementedException(this);
    }

    @Override
    default String getName() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isAdd() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isBackwardsJump() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isConditionalJump() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isForwardsJump() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isLoad() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isLogical() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isStore() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isSub() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isUnary() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isUnconditionalJump() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isAbsoluteJump() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isRelativeJump() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isFloat() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isImmediateJump() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isImmediateValue() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isJump() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isMemory() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isMul() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isUnknown() {
        throw new NotImplementedException(this);
    }

    @Override
    default int getDelay() {
        throw new NotImplementedException(this);
    }

    @Override
    default String getString() {
        return toString();
    }

    @Override
    default AsmFieldData getFieldData() {
        throw new NotImplementedException(this);
    }

    @Override
    default InstructionProperties getProperties() {
        throw new NotImplementedException(this);
    }

    @Override
    default String getRepresentation() {
        throw new NotImplementedException(this);
    }

    @Override
    default Instruction copy() {
        throw new NotImplementedException(this);
    }

    @Override
    default void makeSymbolic(Number address, Map<String, String> regremap) {
        throw new NotImplementedException(this);
    }

    @Override
    default InstructionPseudocode getPseudocode() {
        throw new NotImplementedException(this);
    }
}