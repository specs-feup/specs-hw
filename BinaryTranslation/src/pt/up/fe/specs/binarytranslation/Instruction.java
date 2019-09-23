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

package pt.up.fe.specs.binarytranslation;

import pt.up.fe.specs.binarytranslation.generic.GenericInstruction;

/**
 * Represents a generic assembly instruction.
 * 
 * @author JoaoBispo
 *
 */
public interface Instruction {

    /*
     * Generic instruction types.
     * Specific architecture implementations 
     * must map each instruction in the ISA
     * to one of these types 
     */
    enum InstructionType {
        add,
        sub,
        logical,
        unarylogical,
        jump,
        cjump,
        ujump,
        store,
        load,
        memory
    }
    // TODO add more types

    static Instruction newInstance(String address, String instruction) {
        return new GenericInstruction(address, instruction);
    }

    // Get Fundamental properties /////////////////////////////////////////////
    /*
     * Position of instruction in program memory
     */
    String getAddress();

    /*
     * Binary code of instruction
     */
    String getInstruction();

    /*
     * Gets latency of binary instruction.
     * Retrieved from specific ISA implementation.
     */
    default int getLatency() {
        return 1;
    }

    // Check for instruction type /////////////////////////////////////////////
    /*
     * Check if instruction is backwards branch.
     */
    default boolean isBackwardsBranch() {
        return false;
    }
    // TODO remove default implementation
    // after legacy issues are resolved

    /*
     * Check if instruction is forwards branch.
     */
    default boolean isForwardsBranch() {
        return false;
    }
    // TODO remove default implementation
    // after legacy issues are resolved

    /*
     * Check if instruction is addition
     */
    public boolean isAdd();

    /*
     * Check if instruction is subtraction
     */
    public boolean isSub();

    /*
     * Check if instruction is logical
     */
    public boolean isLogical();

    /*
     * Check if instruction is unary logical
     */
    public boolean isUnaryLogical();

    /*
     * Check if instruction is jump (of any kind)
     */
    default public boolean isJump() {
        return (isConditionalJump() | isUnconditionalJump()
                | isForwardsBranch() | isBackwardsBranch());
    }

    /*
     * Check if instruction is logical
     */
    public boolean isConditionalJump();

    /*
     * Check if instruction is logical
     */
    public boolean isUnconditionalJump();

    /*
     * Check if instruction is store
     */
    public boolean isStore();

    /*
     * Check if instruction is load
     */
    public boolean isLoad();

    /*
     * Check if instruction is memory access
     */
    default public boolean isMemory() {
        return (isStore() | isLoad());
    }

    /**
     * 
     * @param instruction
     * @return the value 0 if the address of the given instruction is equal to the address of this instruction; a value
     *         less than 0 if the address of this instruction is less than the address of given instruction; and a value
     *         greater than 0 if the address of this instruction is greater than the address of given instruction.
     */
    default int compareAddr(Instruction instruction) {
        return getAddress().compareTo(instruction.getAddress());
    }

}
