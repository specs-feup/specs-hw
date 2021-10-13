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
 
package org.specs.MicroBlaze.test.stream;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.processes.GDBRun;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeColorTracerTester {

    private void testColor(ZippedELFProvider elf) {

        var sb = new StringBuilder();
        try (var stream = elf.asTraceTxtDump().toTraceStream()) {
            Instruction inst = null;
            stream.runUntil(stream.getApp().getKernelStart());

            int i = 0;
            while (((inst = stream.nextInstruction()) != null) && i++ < 1000) {

                var code = inst.getName().hashCode();
                var r = (code & 0x000000FF);
                var g = (code & 0x0000FF00) >> 8;
                var b = (code & 0x00FF0000) >> 16;
                sb.append(r + "," + g + "," + b + "\n");

                // System.out.println(inst.getRepresentation());
            }

            var file = new File("./" + elf.getELFName().replace(".elf", "") + "_colormap.txt");
            SpecsIo.write(file, sb.toString());

        } catch (Exception e) {
            // TODO Auto-generated catch block1
            e.printStackTrace();
        }
    }

    private void testColorGDB(ZippedELFProvider elf) {

        var sb = new StringBuilder();
        var app = elf.toApplication();
        try (var gdb = GDBRun.newInstanceInteractive(app)) {

            // launch QEMU, then GDB (on localhost:1234)
            gdb.start();
            // gdb.runUntil(elf.getFunctionName());
            gdb.runUntil("main");

            // stepi
            while (true) { // for (int i = 0; i < 10000; i++) {
                gdb.stepi();

                var addrandinst = gdb.getAddrAndInstruction();
                if (addrandinst == null)
                    break;

                var splits = addrandinst.split(":");
                var inst = splits[1];
                System.out.println(addrandinst);

                var code = inst.hashCode();
                var r = (code & 0x000000FF);
                var g = (code & 0x0000FF00) >> 8;
                var b = (code & 0x00FF0000) >> 16;
                sb.append(r + "," + g + "," + b + "\n");

            }

            var file = new File("./" + elf.getELFName().replace(".elf", "") + "_fulltrace_colormap.txt");
            SpecsIo.write(file, sb.toString());

        } catch (Exception e) {
            // TODO Auto-generated catch block1
            e.printStackTrace();
        }
    }

    @Test
    public void testAllColors() {

        testColorGDB(MicroBlazeLivermoreN100.matmul);

        // for (var elf : Arrays.asList(MicroBlazeLivermoreN100.values()))
        // testColorGDB(elf);
    }
}
