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

import java.util.Optional;

import org.specs.MicroBlaze.legacy.MbRegister;

import pt.up.fe.specs.simulator.impl.AMachine;
import pt.up.fe.specs.util.SpecsCheck;

public class MicroBlazeMachine extends AMachine {

    // Status of delay slot:
    // > 0 - The is a delay slot down the line
    // 0 - Is currently in the last instruction of the delay slot
    // < 0 - Not in a delay slot currently
    int currentDelaySlot;
    Number delaySlotJump;

    // If set, next PC will jump to this value
    Number jump;

    public MicroBlazeMachine() {
        this.currentDelaySlot = -1;
        this.delaySlotJump = null;
        this.jump = null;
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

    public Number advancePC() {
        var nextPC = nextPC();
        getRegisters().write(getPCRegister(), nextPC);
        return nextPC;
    }

}
