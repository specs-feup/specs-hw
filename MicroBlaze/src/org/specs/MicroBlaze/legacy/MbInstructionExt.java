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

/**
 * MbInstruction extended with additional information.
 *
 * @author Joao Bispo
 */
public class MbInstructionExt {

    public MbInstructionExt(MbInstruction inst, Boolean branchTaken, boolean isExit, boolean hasCarryIn,
            boolean hasCarryOut, int targetAddress, int noJumpAddress) {
        this.inst = inst;
        this.branchTaken = branchTaken;
        this.isExit = isExit;
        this.hasCarryIn = hasCarryIn;
        this.hasCarryOut = hasCarryOut;
        this.nextAtomicAddress = targetAddress;
        this.noJumpAddress = noJumpAddress;
    }
    /*
       public MbInstructionExt(MbInstruction inst, Boolean branchTaken, boolean isJump, boolean hasCarryIn, boolean hasCarryOut) {
      this.inst = inst;
      this.branchTaken = branchTaken;
      this.isJump = isJump;
      this.hasCarryIn = hasCarryIn;
      this.hasCarryOut = hasCarryOut;
       }
    */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(inst.toString());
        if (branchTaken != null) {
            builder.append(" Taken?:").append(branchTaken);
        }

        if (isExit) {
            builder.append(" (Exit)");
        }
        return builder.toString();

    }

    // public boolean getExitIf() {
    public Boolean getExitIf() {
        if (branchTaken == null) {
            return null;
        }

        return !branchTaken;
        /*
        if (branchTaken) {
        return false;
        } else {
        return true;
        }
         *
         */
    }

    /**
     * INSTANCE VARIABLES
     */
    public final MbInstruction inst;
    public final Boolean branchTaken;
    public final boolean isExit;
    public final boolean hasCarryIn;
    public final boolean hasCarryOut;
    /**
     * The address of the next instruction that will be executed, after delay slots.
     * <p>
     * Has into account the expected jumps during the execution.
     *
     */
    public final int nextAtomicAddress;
    /**
     * The address of the next instruction if the jump is not taken.
     */
    public final int noJumpAddress;
}
