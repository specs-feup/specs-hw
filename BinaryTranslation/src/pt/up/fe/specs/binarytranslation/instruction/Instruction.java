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

package pt.up.fe.specs.binarytranslation.instruction;

import java.util.Map;

import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;

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
     * Binary code of instruction as string
     */
    String getInstruction();

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

    /*
     * Check if instruction is uknown to us (for now, this 
     * applies to very special instructions, like 
     * system, cache, or barrier instructions)
     */
    public boolean isUnknown();

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

    /*
     * Prints other instruction properties to system output
     */
    public default void printProperties() {
        return;
    };

    /*
     * Returns decoded instruction data
     */
    public InstructionData getData();

    /*
     * Returns enum Which contains instructions properties
     */
    public InstructionProperties getProperties();

    /*
     * Returns Raw instruction fields as produced 
     * by respective parser (AsmInstructionParser)
     */
    public AsmFieldData getFieldData();

    /*
     * Returns a unique hash representation of the instruction
     */
    // public Integer getHash();

    /*
     * Returns an asm string representation of instruction (includes operands) 
     */
    public String getRepresentation();

    /*
     * Return copy of this object
     */
    public Instruction copy();

    /*
     * Used to abstract an instruction away from an 
     * executed representation, to a symbolic representation
     */
    void makeSymbolic(Number address, Map<String, String> regremap);

    /*
     * Returns a readable string representing the readable operation
     * which the instruction implements
     */
    public String express();
}
