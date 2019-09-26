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

import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Represents a generic assembly instruction.
 * 
 * @author JoaoBispo
 *
 */
public interface Instruction {

    // Get Fundamental properties /////////////////////////////////////////////
    /*
     * Position of instruction in program memory
     */
    Number getAddress();

    /*
     * Binary code of instruction
     */
    String getInstruction();

    default Number getInstructionCode() {
        throw new NotImplementedException(this);
    }

    /*
     * Gets latency of binary instruction.
     * Retrieved from specific ISA implementation.
     */
    public int getLatency();

    /*
     * Gets delay of binary instruction.
     * Retrieved from specific ISA implementation.
     */
    public int getDelay();

    /*
     * Gets the plain (isa mnemonic) name of the instruction
     */
    String getName();

    // Check for instruction type /////////////////////////////////////////////
    /*
     * Check if instruction is addition
     */
    public boolean isAdd();

    /*
     * Check if instruction is subtraction
     */
    public boolean isSub();

    /*
     * Check if instruction is multiplication
     */
    public boolean isMul();

    /*
     * Check if instruction is logical
     */
    public boolean isLogical();

    /*
     * Check if instruction is unary
     */
    public boolean isUnary();

    /*
     * Check if instruction is jump (of any kind)
     */
    public boolean isJump();

    /*
     * Check if instruction is conditional jump
     */
    public boolean isConditionalJump();

    /*
     * Check if instruction is unconditional jump
     */
    public boolean isUnconditionalJump();

    /*
     * Check if instruction is relative jump
     */
    public boolean isRelativeJump();

    /*
     * Check if instruction is absolute jump
     */
    public boolean isAbsoluteJump();

    /*
     * Check if instruction uses immediate 
     * value for either relative or absolute jump
     */
    public boolean isImmediate();

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
    public boolean isMemory();

    /*
     * Check if instruction is floating point
     */
    public boolean isFloat();

    /////////////////////////////////////////////// Additional non basic types:
    /*
     * Check if instruction is backwards branch.
     */
    public boolean isBackwardsJump();

    /*
     * Check if instruction is forwards branch.
     */
    public boolean isForwardsJump();

    /*
     * Return target of branch, if this
     * instruction is a branch, else return
     * this instruction addr + the instruction 
     * set's instruction width 
     */
    Number getBranchTarget();

    ///////////////////////////////////////////////////////////////////// Utils
    /*
     * Prints addr:instruction to system output
     */
    public void printInstruction();

    /**
     * 
     * @param instruction
     * @return the value 0 if the address of the given instruction is equal to the address of this instruction; a value
     *         less than 0 if the address of this instruction is less than the address of given instruction; and a value
     *         greater than 0 if the address of this instruction is greater than the address of given instruction.
     */
    default int compareAddr(Instruction instruction) {
        return getAddress().toString().compareTo(instruction.getAddress().toString());
    }
}
