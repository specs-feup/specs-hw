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

import pt.up.fe.specs.binarytranslation.analysis.graphs.pseudocode.PseudoInstructionGraph;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;

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
    default Long getAddress() {
        return null;
    };

    /*
     * Binary code of instruction as string
     */
    default String getInstruction() {
        return null;
    }

    /*
     * Gets latency of binary instruction.
     * Retrieved from specific ISA implementation.
     */
    default int getLatency() {
        return 0;
    };

    /*
     * Gets delay of binary instruction.
     * Retrieved from specific ISA implementation.
     */
    default int getDelay() {
        return 0;
    };

    /*
     * Gets the plain (isa mnemonic) name of the instruction
     */
    default String getName() {
        return null;
    };

    /*
     * 
     */
    default InstructionPseudocode getPseudocode() {
        return null;
    }

    /*
     * Gets the jGraphT version of the pseudocode
     */
    default PseudoInstructionGraph getPseudocodeGraph() {
        return null;
    }

    // Check for instruction type /////////////////////////////////////////////
    /*
     * Check if instruction is addition
     */
    default boolean isAdd() {
        return false;
    }

    /*
     * Check if instruction is subtraction
     */
    default boolean isSub() {
        return false;
    }

    /*
     * Check if instruction is multiplication
     */
    default boolean isMul() {
        return false;
    }

    /*
     * Check if instruction is logical
     */
    default boolean isLogical() {
        return false;
    }

    /*
     * Check if instruction is comparison
     */
    default boolean isComparison() {
        return false;
    }

    /*
     * Check if instruction is unary
     */
    default boolean isUnary() {
        return false;
    }

    /*
     * Check if instruction is jump (of any kind)
     */
    default boolean isJump() {
        return false;
    }

    /*
     * Check if instruction is conditional jump
     */
    default boolean isConditionalJump() {
        return false;
    }

    /*
     * Check if instruction is unconditional jump
     */
    default boolean isUnconditionalJump() {
        return false;
    }

    /*
     * Check if instruction is relative jump
     */
    default boolean isRelativeJump() {
        return false;
    }

    /*
     * Check if instruction is absolute jump
     */
    default boolean isAbsoluteJump() {
        return false;
    }

    /*
     * Check if instruction uses immediate 
     * value for either relative or absolute jump
     */
    default boolean isImmediateJump() {
        return false;
    }

    /*
     * Check if instruction is "imm"
     * (Works for Microblaze so far)
     */
    default boolean isImmediateValue() {
        return false;
    }

    /*
     * Check if instruction is store
     */
    default boolean isStore() {
        return false;
    }

    /*
     * Check if instruction is load
     */
    default boolean isLoad() {
        return false;
    }

    /*
     * Check if instruction is memory access
     */
    default boolean isMemory() {
        return false;
    }

    /*
     * Check if instruction is floating point
     */
    default boolean isFloat() {
        return false;
    }

    /*
     * Check if instruction is uknown to us (for now, this 
     * applies to very special instructions, like 
     * system, cache, or barrier instructions)
     */
    default boolean isUnknown() {
        return false;
    }

    /////////////////////////////////////////////// Additional non basic types:
    /*
     * Check if instruction is backwards branch.
     */
    default boolean isBackwardsJump() {
        return false;
    }

    /*
     * Check if instruction is forwards branch.
     */
    default boolean isForwardsJump() {
        return false;
    }

    /*
     * Return target of branch, if this
     * instruction is a branch, else return
     * this instruction addr + the instruction 
     * set's instruction width 
     
    default Number getBranchTarget() {
        return null;
    }*/

    ///////////////////////////////////////////////////////////////////// Utils
    /*
     * Prints addr:instruction to system output
     */
    default void printInstruction() {
        System.out.println(toString());
    }

    /**
     * 
     * @return string representation of the instruction
     */
    default String getString() {
        return toString();
    }

    /*
     * Prints other instruction properties to system output
     */
    default void printProperties() {
        return;
    }

    /*
     * Returns decoded instruction data
     */
    default AInstructionData getData() {
        return null;
    }

    /*
     * Returns enum Which contains instructions properties
     */
    default InstructionProperties getProperties() {
        return null;
    }

    /*
     * Returns Raw instruction fields as produced 
     * by respective parser (AsmInstructionParser)
     */
    /*default AsmFieldData getFieldData() {
        return null;
    }*/

    /*makeSymbolic
     * Returns an asm string representation of instruction (includes operands) 
     */
    default String getRepresentation() {
        return null;
    }

    /*
     * Return copy of this object
     */
    default Instruction copy() {
        return null;
    }

    /*
     * Used to abstract an instruction away from an 
     * executed representation, to a symbolic representation
     */
    default void makeSymbolic(Integer address, Map<String, String> regremap) {
        return;
    }

    default RegisterDump getRegisters() {
        return null;
    }

    default void setRegisters(RegisterDump registers) {
        return;
    }
}
