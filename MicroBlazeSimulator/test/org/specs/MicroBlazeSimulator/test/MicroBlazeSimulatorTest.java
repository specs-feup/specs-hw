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

package org.specs.MicroBlazeSimulator.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlazeSimulator.microcode.MbMicroCodeUtils;
import org.specs.MicroBlazeSimulator.simulator.MbInstsConverter;
import org.specs.MicroBlazeSimulator.simulator.MicroBlazeMachine32;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.simulator.Addr;
import pt.up.fe.specs.simulator.SimInstruction;
import pt.up.fe.specs.simulator.SimulatorV2;
import pt.up.fe.specs.simulator.microcodemachine.MicroCodeMachine;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;

public class MicroBlazeSimulatorTest {

    @BeforeClass
    public static void init() {
        SpecsSystem.programStandardInit();
    }

    @Test
    public void testTinyMicroblazeSimulator() {

        // Get instructions
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/tiny.txt");
        fd.deleteOnExit();

        // Instructions converter
        var machine = new MicroBlazeMachine32();
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

        assertEquals("", simulator.getMachine().getMemory().toString());
        assertEquals("rpc: 128", simulator.getMachine().getRegisters().toString());
    }

    @Test
    public void testTinyMbMicroCodeSimulator() {

        // Create machine
        MicroCodeMachine microBlaze32 = MbMicroCodeUtils.newMicroBlaze32();

        // Get instructions
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/tiny.txt");
        fd.deleteOnExit();

        Map<Addr, Instruction> program = new HashMap<>();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            while (el.hasNext()) {
                var inst = el.nextInstruction();

                // Last instruction can be null
                if (inst == null) {
                    continue;
                }

                Addr address = microBlaze32.toAddr(inst.getAddress());
                program.put(address, inst);

            }
        }

        // Load program and run
        microBlaze32.loadProgram(program);
        microBlaze32.run();
    }

}
