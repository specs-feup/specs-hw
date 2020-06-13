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

import org.specs.MicroBlaze.legacy.MbRegister;

import pt.up.fe.specs.simulator.SimInstruction;
import pt.up.fe.specs.simulator.impl.AMachine;
import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;

public abstract class MicroBlazeMachine<Addr extends Number> extends AMachine {

    private final Map<Addr, MbSimInstruction> instructions;

    private Addr currentAddr;
    private Addr nextAddr;

    // Status of delay slot:
    // > 0 - Currently in a delay slot queue
    private int currentDelaySlot;
    // private Number delaySlotJump;

    // If set, next PC will jump to this value
    private Addr jump;

    // IMM value
    private Integer immValue;

    public MicroBlazeMachine() {
        instructions = new HashMap<>();

        // No current instruction yet
        currentAddr = null;
        // By default, start address is 0
        nextAddr = toAddr(0);

        this.currentDelaySlot = 0;
        // this.delaySlotJump = null;
        this.jump = null;

        this.immValue = null;
    }

    protected abstract Addr toAddr(Number number);

    protected abstract int getAddrBitwidth();

    public void addInstruction(MbSimInstruction instruction) {
        var previousValue = instructions.put(toAddr(instruction.getAddress()), instruction);

        if (previousValue != null) {
            SpecsLogs.info("Overwriting instruction at address '" + instruction.getAddress() + "'\nPrevious value: '"
                    + previousValue + "'\nNew value: '" + instruction + "'");
        }
    }

    public Integer getImmValue() {
        return immValue;
    }

    /**
     * @return true if current instruction is the same as the next instruction, false otherwise
     */
    @Override
    public boolean hasStopped() {
        if (currentAddr == null) {
            return false;
        }

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
        this.nextAddr = toAddr(startAddress);
    }

    private SimInstruction getInstruction(Number address) {
        var inst = instructions.get(toAddr(address));
        // System.out.println("INSTRUC: " + instructions);
        SpecsCheck.checkNotNull(inst,
                () -> "No instruction found at address " + address + " (" +
                        SpecsStrings.toHexString(address.longValue(), getAddrBitwidth() / 4) + ")");

        return inst;
    }

    public Number getPCRegister() {
        return MbRegister.RPC.ordinal();
    }

    /**
     * 
     * 
     * @return the next PC address if we are at the last instruction of a delay slot, empty otherwise.
     */
    /*
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
    */

    /**
     * 
     * @return true if currently in a delay queue
     */
    private boolean inDelaySlotQueue() {
        boolean inDelayQueue = currentDelaySlot > 0;

        if (inDelayQueue) {
            currentDelaySlot--;
        }

        return inDelayQueue;

        /*
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
        */
    }

    private Number nextPC() {
        // If in delay slot, just execute next instruction
        if (inDelaySlotQueue()) {
            // Current address + 4
            return currentAddr.intValue() + 4;
        }

        // // Delay slot
        // var delaySlotJump = inDelaySlotQueue();
        // if (delaySlotJump.isPresent()) {
        // return delaySlotJump.get();
        // }

        // If there is a jump scheduled, return it
        if (jump != null) {
            var jumpAddr = jump;
            jump = null;
            return jumpAddr;
        }

        // Current address + 4
        return currentAddr.intValue() + 4;
        // var currentPC = getRegisters().read(getPCRegister());
        // var nextPC = currentPC.intValue() + 4;
        //
        // return nextPC;
    }

    public Number updatePC() {
        nextAddr = toAddr(nextPC());
        // var nextPC = nextPC();
        getRegisters().write(getPCRegister(), nextAddr);
        return nextAddr;
    }

    public void setImmValue(int immValue) {
        SpecsCheck.checkArgument(this.immValue == null,
                () -> "Current imm value is not null (" + this.immValue + "), tried to set it with value " + immValue);

        this.immValue = immValue;
    }

    /**
     * Sign-extend imm, taking into account previous IMM instructions.
     * 
     * <p>
     * If there is a value of IMM set, it will be unset after this instruction.
     * 
     * @param imm
     * @return
     */
    public int signExtendImm(int imm) {
        // If there is a IMM value set, shift it and mix with given imm
        if (immValue != null) {
            var upperImm = immValue << 16;
            var completeImm = upperImm | imm;

            // Clear immValue
            immValue = null;

            return completeImm;
        }

        // Check if immediate value is negative
        if ((imm & 0x8000) == 0x8000) {
            SpecsCheck.checkArgument(imm < 0, () -> "Expected value to be negative: " + imm);

            // Sign extend
            return imm | 0xffff0000;
        }

        // No sign-extension needed
        return imm;
    }

    public void setDelaySlot() {
        SpecsCheck.checkArgument(currentDelaySlot < 0,
                () -> "Expected delay slot to be less than 0: " + currentDelaySlot);
        this.currentDelaySlot = 1;
    }

    public void setJump(int jumpAddr) {
        SpecsCheck.checkArgument(jump == null, () -> "Expected jump to be null, is " + jump);

        this.jump = toAddr(jumpAddr);

    }

}
