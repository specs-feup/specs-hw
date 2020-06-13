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

    private final Number address;

    public ASimInstruction(Number address) {
        this.address = address;
    }

    @Override
    public Number getAddress() {
        return address;
    }

    @Override
    public void execute(Machine machine) {
        // Execute proper
        executeProper(machine);

        // Increment PC
        updatePC(machine);

    }

    protected abstract void executeProper(Machine machine);

    /**
     * By default increments PC by 4.
     * 
     * @param machine
     */
    protected void updatePC(Machine machine) {
        var pcReg = machine.getPCRegister();
        // var inc = machine.getPCIncrement();
        // var newPC = machine.getRegisters().read(pcReg).longValue() + inc;
        var newPC = machine.getRegisters().read(pcReg).longValue() + 4;
        machine.getRegisters().write(pcReg, newPC);
    }
}
