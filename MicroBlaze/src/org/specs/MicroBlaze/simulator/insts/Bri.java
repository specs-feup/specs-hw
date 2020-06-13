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

package org.specs.MicroBlaze.simulator.insts;

import org.specs.MicroBlaze.simulator.MbSimInstruction;
import org.specs.MicroBlaze.simulator.MicroBlazeMachine;

import pt.up.fe.specs.simulator.Addr;

public class Bri extends MbSimInstruction {

    private final int imm;
    private final Addr linkRegister;

    private final boolean isAbsolute;
    private final boolean hasDelaySlot;

    public Bri(MicroBlazeMachine machine, Number address, int imm, Number linkRegister, boolean isAbsolute,
            boolean hasDelaySlot) {

        super(machine, machine.toAddr(address));
        this.imm = imm;
        this.linkRegister = machine.toAddr(linkRegister);

        this.isAbsolute = isAbsolute;
        this.hasDelaySlot = hasDelaySlot;
    }

    public static Bri brai(MicroBlazeMachine machine, Number address, int imm) {
        return new Bri(machine, address, imm, null, true, false);
    }

    public static Bri bri(MicroBlazeMachine machine, Number address, int imm) {
        return new Bri(machine, address, imm, null, false, false);
    }

    @Override
    protected void executeProper() {
        // System.out.println("BRI IMM Original: " + imm);
        if (linkRegister != null) {
            getMachine().getRegisters().write(linkRegister, getAddress().toNumber());
        }

        // System.out.println("IMM INST: " + getMachine().getImmValue());
        var adjustedImm = getMachine().signExtendImm(imm);
        // System.out.println("BRI IMM Adjusted: " + adjustedImm);

        if (hasDelaySlot) {
            // Set delay slot
            getMachine().setDelaySlot();
        }

        // Calculate jump
        // var jumpTarget = isAbsolute ? adjustedImm : getAddress().intValue() + adjustedImm;
        var jumpTarget = isAbsolute ? getMachine().toAddr(adjustedImm) : getAddress().add(adjustedImm);
        // System.out.println("BRI JUMP: " + jumpTarget);
        getMachine().setJump(jumpTarget);

    }

}
