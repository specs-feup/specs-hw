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

package pt.up.fe.specs.simulator.impl;

import pt.up.fe.specs.simulator.Machine;
import pt.up.fe.specs.simulator.MemoryElement;

public abstract class AMachine implements Machine {

    private final MemoryElement registers;
    private final MemoryElement memory;

    public AMachine() {
        this.registers = new GenericMemoryElement("Registers");
        this.memory = new GenericMemoryElement("RAM");
    }

    @Override
    public MemoryElement getMemory() {
        return memory;
    }

    @Override
    public MemoryElement getRegisters() {
        return registers;
    }

}