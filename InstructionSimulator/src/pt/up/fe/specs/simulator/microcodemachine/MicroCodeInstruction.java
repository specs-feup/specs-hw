/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.simulator.microcodemachine;

import java.util.function.Consumer;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.simulator.ASimInstruction;

public class MicroCodeInstruction extends ASimInstruction {

    private final Instruction originalInstruction;
    private final Consumer<MicroCodeInstruction> microCode;

    public MicroCodeInstruction(MicroCodeMachine machine, Instruction originalInstruction,
            Consumer<MicroCodeInstruction> microCode) {

        super(machine, machine.toAddr(originalInstruction.getAddress()));

        this.originalInstruction = originalInstruction;
        this.microCode = microCode;
    }

    public Instruction getOriginalInstruction() {
        return originalInstruction;
    }

    @Override
    public MicroCodeMachine getMachine() {
        return (MicroCodeMachine) super.getMachine();
    }

    @Override
    public void execute() {
        // Execute instruction
        microCode.accept(this);

        // Calculate next addr
        getMachine().updatePC();
    }

    // public void preloadImm(Number value) {
    // getMachine().setPreloadedImm(value);
    // }

    /**
     * 
     * 
     * @param immValue
     * @return
     */
    // public void signExtendImm32(int immValue) {
    // // TODO Auto-generated method stub
    // return null;
    // }

    /**
     * Sign-extend imm, taking into account previous IMM instructions.
     * 
     * <p>
     * If there is a value of IMM set, it will be unset after this instruction.
     * 
     * @param imm
     * @return
     */
    // public int signExtendImm(int imm) {
    // // If there is a IMM value set, shift it and mix with given imm
    // if (immValue != null) {
    // var upperImm = immValue << 16;
    // var completeImm = upperImm | imm;
    //
    // // Clear immValue
    // immValue = null;
    //
    // return completeImm;
    // }
    //
    // // Check if immediate value is negative
    // if ((imm & 0x8000) == 0x8000) {
    // SpecsCheck.checkArgument(imm < 0, () -> "Expected value to be negative: " + imm);
    //
    // // Sign extend
    // return imm | 0xffff0000;
    // }
    //
    // // No sign-extension needed
    // return imm;
    // }

}
