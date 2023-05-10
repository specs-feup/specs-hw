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
 
package pt.up.fe.specs.binarytranslation.utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import pt.up.fe.specs.binarytranslation.elf.ELFProvider;
import pt.up.fe.specs.binarytranslation.processes.GDBRun;

/**
 * Receives a @TraceInstructionProducer and saves a specified snippet of the trace to a file, this makes it easier to
 * parse in only that snippet to generate graphs and test other stuff
 * 
 * @author Nuno
 *
 */
public class TraceSnipper {

    /* 
     * Static map of ELFProvider types to Application child class
     */

    /*
     * Must snip from a live run
     */
    public static void snipTrace(ELFProvider elf, Long nrInsts) {

        var app = elf.toApplication();
        var from = app.getKernelStart();
        var filename = app.getAppName().replace(".elf", "_0x"
                + Long.toHexString(from) + "to0x"
                + Long.toHexString(from + nrInsts * app.getInstructionWidth()));

        try (var gdb = GDBRun.newInstanceInteractive(app)) {

            gdb.runUntil(from);

            var fos = new FileWriter(filename);
            var bw = new BufferedWriter(fos);

            int i = 0;
            String line = null;
            do {
                gdb.stepi();
                line = gdb.getAddrAndInstruction();
                if (line != null) {
                    bw.write(line + "\n");
                    bw.flush();
                }
            } while (line != null && i++ < nrInsts);
            bw.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
