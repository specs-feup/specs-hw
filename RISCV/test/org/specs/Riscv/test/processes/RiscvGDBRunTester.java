/**
 * Copyright 2021 SPeCS.
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
 
package org.specs.Riscv.test.processes;

import java.util.Arrays;

import org.junit.Test;
import org.specs.Riscv.asm.RiscvELFDump;
import org.specs.Riscv.provider.RiscvLivermoreN100imaf;

import pt.up.fe.specs.binarytranslation.test.processes.GDBRunTester;

public class RiscvGDBRunTester extends GDBRunTester {

    /**
     * Test the GDBRun class by giving it a script and consuming all output
     */
    @Test
    public void testScript() {
        var elf = RiscvLivermoreN100imaf.matmul;
        testScript(Arrays.asList(elf));
    }

    /**
     * Test runUntil function start
     */
    @Test
    public void testRunToKernelStart() {
        var elfs = RiscvLivermoreN100imaf.values();
        testRunToKernelStart(Arrays.asList(elfs));
    }

    /**
     * 
     */
    @Test
    public void testGDBFeatures() {
        var elf = RiscvLivermoreN100imaf.matmul;
        testGDBFeatures(Arrays.asList(elf));
    }

    @Test
    public void testELFDump() {
        var dump = new RiscvELFDump(RiscvLivermoreN100imaf.matmul);
        System.out.println(dump.getInstruction(Long.valueOf("80", 16)));
    }
}
