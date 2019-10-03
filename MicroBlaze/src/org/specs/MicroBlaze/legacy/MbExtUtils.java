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

package org.specs.MicroBlaze.legacy;

import java.util.List;
import java.util.Optional;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Utility methods for parsing Microblaze assembly into a Basic MegablockGraph.
 *
 * @author Joao Bispo
 */
public class MbExtUtils {

    /**
     * The address of the next instruction that will be executed, after delay slots.
     * 
     * @param mbInsts
     * @param index
     * @return
     */
    public static int getNextAtomicAddress(List<MbInstruction> mbInsts, int index) {
        MbInstruction branch = mbInsts.get(index);

        // How many delay slots?
        int delaySlots = OperationProperties.getDelaySlots(branch.getInstructionName());
        int targetAddressIndex = index + delaySlots + 1;

        int realTargetAddress = SpecsStrings.moduloGet(mbInsts, targetAddressIndex).getAddress32();

        return realTargetAddress;
    }

    /**
     * The address to where execution goes if the jump is not taken.
     *
     * @param mbInsts
     * @param index
     * @return
     */
    public static int getNoJumpAddress(List<MbInstruction> mbInsts, int index) {
        MbInstruction branch = mbInsts.get(index);
        int addressOffset = 4;

        // How many delay slots?
        int delaySlots = OperationProperties.getDelaySlots(branch.getInstructionName());

        return mbInsts.get(index).getAddress32() + (delaySlots + 1) * addressOffset;
    }

    /**
     * Given a list of executed instructions, determines if the branch at the given index was taken or not.
     *
     * @param mbInst
     * @param asmData
     * @return the direction of the branch. Returns true if the branch is taken, returns false if it is not taken.
     *         Returns null if the instruction is not a branch
     */
    public static Boolean getExitDirection(List<MbInstruction> mbInsts, int index) {
        MbInstruction branch = mbInsts.get(index);
        if (!OperationProperties.isJump(branch.getInstructionName())) {
            return null;
        }

        // How many delay slots?
        int delaySlots = OperationProperties.getDelaySlots(branch.getInstructionName());

        int addressOffset = 4;
        int targetAddressIndex = index + delaySlots + 1;

        int noJumpTargetAddress = mbInsts.get(index).getAddress32() + (delaySlots + 1) * addressOffset;
        int realTargetAddress = SpecsStrings.moduloGet(mbInsts, targetAddressIndex).getAddress32();

        // Check if there is no jump
        if (realTargetAddress == noJumpTargetAddress) {
            return false;
        }
        return true;
    }

    /**
     * Due to delay slots in Microblaze architecture, branch instructions might not be the exit of a block.
     *
     * @param mbInsts
     * @param index
     * @return true if the instruction represents an exit. False otherwise.
     */
    public static boolean isExit(List<MbInstruction> mbInsts, int index) {
        MbInstruction inst = mbInsts.get(index);

        // Check if jump instruction
        if (OperationProperties.isJump(inst.getInstructionName())) {
            // Check if it has delay slots
            int delaySlots = OperationProperties.getDelaySlots(inst.getInstructionName());
            if (delaySlots == 0) {
                return true;
            }

            return false;

        }

        // Check if previous instruction is delay slot. Microblaze architecture
        // has at most one delay slot.
        MbInstruction possibleBranch = SpecsStrings.moduloGet(mbInsts, index - 1);
        if (!OperationProperties.isJump(possibleBranch.getInstructionName())) {
            return false;
        }

        int delaySlots = OperationProperties.getDelaySlots(possibleBranch.getInstructionName());
        if (delaySlots == 1) {
            return true;
        }

        if (delaySlots > 1) {
            SpecsLogs.getLogger()
                    .warning("Found delay slot greater than one. This method only supports up to one delay slots.");
        }

        return false;
    }

    /**
     * Replaces occurences of R0 for 0.
     * 
     * @param instructions
     * @return
     */
    public static void replaceR0(List<MbInstruction> instructions) {
        for (MbInstruction instruction : instructions) {
            List<MbOperand> operands = instruction.getOperands();
            for (int i = 0; i < operands.size(); i++) {
                Optional<MbOperand> imm0 = MbOperand.transformR0InImm0(operands.get(i));
                if (!imm0.isPresent()) {
                    continue;
                }

                operands.set(i, imm0.get());
            }
        }
    }
}
