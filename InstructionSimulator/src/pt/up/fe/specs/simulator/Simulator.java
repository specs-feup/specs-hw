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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsLogs;

public class Simulator {

    private final Machine machine;
    private final Map<Number, SimInstruction> instructions;

    public Simulator(Machine machine) {
        this.machine = machine;
        this.instructions = new HashMap<>();
    }

    public void setInstructions(Collection<SimInstruction> instructions) {
        if (this.instructions.isEmpty()) {
            this.instructions.clear();
        }

        for (var inst : instructions) {
            this.instructions.put(inst.getAddress(), inst);
        }

    }

    public void execute(Number startAddress) {
        // Get instruction
        SimInstruction currentInstruction = getInstruction(startAddress);

        // PC register
        var pcReg = machine.getPCRegister();

        // Set PC
        machine.getRegisters().write(pcReg, startAddress);

        // Stop when the same instruction is executed twice (e.g., jump to itself)
        while (true) {

            // Execute
            currentInstruction.execute(machine);

            // Get next instruction
            var nextInst = machine.getRegisters().read(pcReg);

            // Check if should stop
            if (nextInst.equals(currentInstruction.getAddress())) {
                break;
            }

            // Update next instruction
            currentInstruction = instructions.get(nextInst);
        }

        SpecsLogs.info("Simulation ended");
    }

    private SimInstruction getInstruction(Number startAddress) {
        var inst = instructions.get(startAddress);

        SpecsCheck.checkNotNull(inst,
                () -> "No instruction found at address " + Long.toHexString(startAddress.longValue()));

        return inst;
    }

}
