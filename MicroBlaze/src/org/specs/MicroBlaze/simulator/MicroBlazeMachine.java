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

package org.specs.MicroBlaze.simulator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.specs.MicroBlaze.legacy.MbRegister;

import pt.up.fe.specs.simulator.SimInstruction;
import pt.up.fe.specs.simulator.impl.AMachine;
import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsLogs;

public class MicroBlazeMachine extends AMachine {

    private final Map<Number, MbSimInstruction> instructions;

    private Number currentAddr;
    private Number nextAddr;

    // Status of delay slot:
    // > 0 - The is a delay slot down the line
    // 0 - Is currently in the last instruction of the delay slot
    // < 0 - Not in a delay slot currently
    private int currentDelaySlot;
    private Number delaySlotJump;

    // If set, next PC will jump to this value
    private Number jump;

    public MicroBlazeMachine() {
        instructions = new HashMap<>();

        // No current instruction yet
        currentAddr = null;
        // By default, start address is 0
        nextAddr = 0;

        this.currentDelaySlot = -1;
        this.delaySlotJump = null;
        this.jump = null;
    }

    public void addInstruction(MbSimInstruction instruction) {
        var previousValue = instructions.put(instruction.getAddress(), instruction);

        if (previousValue != null) {
            SpecsLogs.info("Overwriting instruction at address '" + instruction.getAddress() + "'\nPrevious value: '"
                    + previousValue + "'\nNew value: '" + instruction + "'");
        }
    }

    /**
     * @return true if current instruction is the same as the next instruction, false otherwise
     */
    @Override
    public boolean hasStopped() {
        return currentAddr.equals(nextAddr);
    }

    @Override
    public SimInstruction nextInstruction() {
        if (nextAddr == null) {
            throw new RuntimeException(
                    "Not address set for next instruction. Make sure previous instruction has executed");
        }

        // Update addresses
        currentAddr = nextAddr;
        nextAddr = null;

        // Get instruction
        return getInstruction(currentAddr);
    }

    @Override
    public void setStartAddress(Number startAddress) {
        this.currentAddr = null;
        this.nextAddr = startAddress;
    }

    private SimInstruction getInstruction(Number address) {
        var inst = instructions.get(address);

        SpecsCheck.checkNotNull(inst,
                () -> "No instruction found at address " + Long.toHexString(address.longValue()));

        return inst;
    }

    @Override
    public Number getPCRegister() {
        return MbRegister.RPC.ordinal();
    }

    /**
     * 
     * 
     * @return the next PC address if we are at the last instruction of a delay slot, empty otherwise.
     */
    private Optional<Number> processDelaySlot() {
        // Not in a delay slot
        if (currentDelaySlot < 0) {
            return Optional.empty();
        }

        // In a delay slot, but not the last instruction
        if (currentDelaySlot > 0) {
            currentDelaySlot--;
            return Optional.empty();
        }

        // In a delay slot
        SpecsCheck.checkArgument(jump == null, () -> "Currently in a delay slot, 'jump' should be null but is " + jump);

        var jumpAddr = delaySlotJump;
        delaySlotJump = null;

        return Optional.of(jumpAddr);
    }

    private Number nextPC() {
        // Delay slot
        var delaySlotJump = processDelaySlot();
        if (delaySlotJump.isPresent()) {
            return delaySlotJump.get();
        }

        // Jump
        if (jump != null) {
            var jumpAddr = jump;
            jump = null;
            return jumpAddr;
        }

        // Current address + 4
        var currentPC = getRegisters().read(getPCRegister());
        var nextPC = currentPC.intValue() + 4;

        return nextPC;
    }

    public Number updatePC() {
        nextAddr = nextPC();
        // var nextPC = nextPC();
        getRegisters().write(getPCRegister(), nextAddr);
        return nextAddr;
    }

}
