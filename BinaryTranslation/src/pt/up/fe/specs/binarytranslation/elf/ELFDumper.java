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
 
package pt.up.fe.specs.binarytranslation.elf;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.processes.GDBRun;
import pt.up.fe.specs.binarytranslation.processes.ObjDump;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class ELFDumper {

    /*
     * 
     */
    private static void dumpELFStatic(ELFProvider elf) {
        var app = elf.toApplication();
        var objdump = new ObjDump(app);
        var outputname = elf.getELFName().replace(".elf", "_objdump.txt");
        objdump.start();
        BinaryTranslationUtils.dumpProcessRunStdOut(objdump, outputname);
    }

    /*
     * Dumps at most 200k insts to file, starting from the kernel start position
     */
    private static void dumpELFTrace(ELFProvider elf) {
        var app = elf.toApplication();
        var outputname = elf.getELFName().replace(".elf", "_trace.txt");

        /*
         * gdb is AutoClosable
         */
        try (var gdb = GDBRun.newInstanceInteractive(app)) {

            // run until kernel start
            gdb.start();
            gdb.runUntil(elf.getFunctionName());

            var fos = new FileWriter(outputname);
            var bw = new BufferedWriter(fos);

            // for (int i = 0; i < 10; i++) {
            for (int i = 0; i < 1000; i++) {
                gdb.stepi();
                var instlline = gdb.getAddrAndInstruction();
                if (instlline == null)
                    break;
                bw.write("0x" + instlline.replace(":", " <>: 0x") + "\n");
                // needs this to rematch with regex later
                bw.flush();
            }
            bw.close();
            fos.close();

        } catch (

        FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param elfs
     */
    protected static void sequentialDump(List<ELFProvider> elfs) {
        for (var elf : elfs) {
            System.out.println("Dumping " + elf.getELFName());
            dumpELFStatic(elf);
            dumpELFTrace(elf);
        }
    }

    /**
     * 
     * @param elfs
     */
    protected static void parallelDump(List<ELFProvider> elfs) {

        var exec = Executors.newFixedThreadPool(elfs.size());

        for (var elf : elfs) {

            try {
                exec.execute(() -> dumpELFStatic(elf));
                exec.execute(() -> dumpELFTrace(elf));
                TimeUnit.SECONDS.sleep(1);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            exec.shutdown();
            exec.awaitTermination(20, TimeUnit.MINUTES);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
