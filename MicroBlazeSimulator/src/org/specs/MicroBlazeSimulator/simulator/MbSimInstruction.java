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

package org.specs.MicroBlazeSimulator.simulator;

import pt.up.fe.specs.simulator.ASimInstruction;
import pt.up.fe.specs.simulator.Addr;

public abstract class MbSimInstruction extends ASimInstruction {

    public MbSimInstruction(MicroBlazeMachine machine, Addr address) {
        super(machine, address);
    }

    @Override
    public MicroBlazeMachine getMachine() {
        return (MicroBlazeMachine) super.getMachine();
    }

    // protected Number writeReg(Number reg, int value) {
    // getMachine().getRegisters().write(reg, value);
    // }

    @Override
    public void execute() {
        // Execute instruction
        executeProper();

        // Calculate next addr
        getMachine().updatePC();
    }

    protected abstract void executeProper();

    // @Override
    // protected void updatePC() {
    // getMachine().advancePC();
    // // var nextPC = nextPC();
    // // getMachine().getRegisters().write(getPCReg(), nextPC);
    // }

}
