package org.specs.MicroBlaze.test.asm;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN10;

import pt.up.fe.specs.binarytranslation.processes.GDBRun;
import pt.up.fe.specs.binarytranslation.processes.ObjDump;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeELFDumper {

    private void dumpELFStatic(MicroBlazeELFProvider elf) {
        var app = new MicroBlazeApplication(elf);
        var objdump = new ObjDump(app);
        var outputname = elf.getELFName().replace(".elf", "_objdump.txt");
        BinaryTranslationUtils.dumpProcessRunStdOut(objdump, outputname);
    }

    /*
     * Dumps at most 200k insts to file, starting from the kernel start position
     */
    private void dumpELFTrace(MicroBlazeELFProvider elf) {
        var app = new MicroBlazeApplication(elf);
        var outputname = elf.getELFName().replace(".elf", "_trace.txt");

        /*
         * gdb is AutoClosable
         */
        try (var gdb = GDBRun.newInstanceInteractive(app)) {

            // run until kernel start
            gdb.runUntil(elf.getKernelStart().toString());

            var fos = new FileWriter(outputname);
            var bw = new BufferedWriter(fos);

            for (int i = 0; i < 200000; i++) {
                gdb.stepi();
                var instlline = gdb.getAddrAndInstruction();
                if (instlline == null)
                    break;
                bw.write("0x" + instlline.replace(":", " <>: 0x") + "\n"); // needs this to rematch with regex later
                bw.flush();
            }
            bw.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dumpELFstoFiles() {

        var elfs = Arrays.asList(MicroBlazeLivermoreELFN10.cholesky);
        // var elfs MicroBlazePolyBenchSmallFloat.values();
        // var elfs = MicroBlazePolyBenchSmallInt.values();

        for (var elf : elfs) {
            // dumpELFStatic(elf);
            dumpELFTrace(elf);
        }
    }
}
