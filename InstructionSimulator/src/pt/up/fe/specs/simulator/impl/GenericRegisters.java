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

import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.simulator.Registers;

public class GenericRegisters implements Registers {

    private final Map<String, Number> registers;

    public GenericRegisters() {
        this.registers = new HashMap<>();
    }

    @Override
    public Number read(String register) {
        var value = registers.get(register);

        // If null, assume value is zero an initialize
        if (value == null) {
            value = 0;
            registers.put(register, value);
        }

        return value;
    }

    @Override
    public Number write(String register, Number value) {
        return registers.put(register, value);
    }

}
