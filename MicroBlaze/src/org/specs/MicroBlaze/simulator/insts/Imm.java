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

import pt.up.fe.specs.simulator.ASimInstruction;
import pt.up.fe.specs.simulator.Machine;

public class Imm extends ASimInstruction {

    private final int immValue;

    public Imm(Number address, int immValue) {
        super(address);

        this.immValue = immValue;
    }

    @Override
    protected void executeProper(Machine machine) {
        // Set register value

    }

}
