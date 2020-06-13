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

package pt.up.fe.specs.simulator;

public abstract class ASimInstruction implements SimInstruction {

    private final Machine machine;
    private final Addr address;

    public ASimInstruction(Machine machine, Addr address) {
        this.machine = machine;
        this.address = address;
    }

    @Override
    public Machine getMachine() {
        return machine;
    }

    @Override
    public Addr getAddress() {
        return address;
    }

    // @Override
    // public void execute() {
    // // Execute proper
    // executeProper();
    //
    // // Increment PC
    // updatePC();
    //
    // }
    //
    // protected abstract void executeProper();
    //
    // /**
    // * By default increments PC by 4.
    // *
    // * @param machine
    // */
    // protected void updatePC() {
    // var pcReg = getMachine().getPCRegister();
    // // var inc = machine.getPCIncrement();
    // // var newPC = machine.getRegisters().read(pcReg).longValue() + inc;
    // var newPC = getMachine().getRegisters().read(pcReg).longValue() + 4;
    // getMachine().getRegisters().write(pcReg, newPC);
    // }
}
