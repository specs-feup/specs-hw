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

package org.specs.MicroBlaze.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.simulator.MbInstsConverter;
import org.specs.MicroBlaze.simulator.MicroBlazeMachine;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.simulator.SimInstruction;
import pt.up.fe.specs.simulator.SimulatorV2;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeSimulatorTest {

    @Test
    public void test() {

        // Get instructions
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/tiny.txt");
        fd.deleteOnExit();

        // Instructions converter
        var machine = new MicroBlazeMachine();
        MbInstsConverter converter = new MbInstsConverter(machine);

        List<SimInstruction> simInsts = new ArrayList<>();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            while (el.hasNext()) {
                var inst = el.nextInstruction();

                // Last instruction can be null
                if (inst == null) {
                    continue;
                }

                simInsts.add(converter.convert(inst));

                // System.out.println("INST: " + inst.getName());
            }
        }

        // var simulator = new Simulator(simInsts);
        var simulator = new SimulatorV2(machine);
        simulator.execute(0);
    }

}
