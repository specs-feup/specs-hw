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
 
package org.specs.MicroBlaze.test.processes;

import java.util.Arrays;

import org.junit.Test;
import org.specs.MicroBlaze.asm.MicroBlazeELFDump;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;

import pt.up.fe.specs.binarytranslation.test.processes.GDBRunTester;

public class MicroBlazeGDBRunTester extends GDBRunTester {

    /*
     * set confirm off
        undisplay
        set print address off
        set height 0
        file <ELFNAME>
        
        
    MB:        target remote | <QEMUBIN> -nographic -M microblaze-fdt-plnx 
    -m 64 -display none -kernel <ELFNAME> -dtb <DTBFILE> -chardev stdio,mux=on,id=char0 
    -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
    
    ARM        target remote | <QEMUBIN> -M virt -cpu cortex-a53 -nographic 
    -kernel <ELFNAME> -chardev stdio,mux=on,id=char0 
    -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
     */

    /**
     * Test the GDBRun class by giving it a script and consuming all output
     */
    @Test
    public void testScript() {
        var elf = MicroBlazeLivermoreN100.matmul;
        testScript(Arrays.asList(elf));
    }

    /**
     * Test runUntil function start
     */
    @Test
    public void testRunToKernelStart() {
        var elfs = MicroBlazeLivermoreN100.values();
        testRunToKernelStart(Arrays.asList(elfs));
    }

    /**
     * 
     */
    @Test
    public void testGDBFeatures() {
        var elf = MicroBlazeLivermoreN100.matmul;
        testGDBFeatures(Arrays.asList(elf));
    }

    @Test
    public void testELFDump() {
        var dump = new MicroBlazeELFDump(MicroBlazeLivermoreN100.matmul);
        System.out.println(dump.getInstruction(Long.valueOf("80", 16)));
    }
}
