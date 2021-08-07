package org.specs.MicroBlaze.test.asm;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniInt;

import pt.up.fe.specs.binarytranslation.processes.GDBRun;
import pt.up.fe.specs.binarytranslation.processes.ObjDump;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeELFDumper {

    private void dumpELFStatic(MicroBlazeELFProvider elf) {
        var app = new MicroBlazeApplication(elf);
        var objdump = new ObjDump(app);
        var outputname = elf.getELFName().replace(".elf", "_objdump.txt");
        objdump.start();
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
            gdb.start();
            gdb.runUntil(elf.getKernelStart());

            var fos = new FileWriter(outputname);
            var bw = new BufferedWriter(fos);

            // for (int i = 0; i < 10; i++) {
            for (int i = 0; i < 100; i++) {
                gdb.stepi();
                var instlline = gdb.getAddrAndInstruction();
                if (instlline == null)
                    break;
                bw.write("0x" + instlline.replace(":", " <>: 0x") + "\n"); // needs this to rematch with regex later
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

    @Test
    public void sequentialDump() {

        // var elfs = Arrays.asList(MicroBlazeLivermoreELFN100.values());
        var elfs = Arrays.asList(MicroBlazePolyBenchMiniInt.values());
        // var elfs = Arrays.asList(MicroBlazePolyBenchSmallInt.lu);

        for (var elf : elfs) {
            System.out.println("Dumping " + elf.getELFName());
            // dumpELFStatic(elf);
            dumpELFTrace(elf);
        }
    }

    @Test
    public void parallelDump() {

        var elfs = Arrays.asList(MicroBlazePolyBenchMiniInt.values());
        var exec = Executors.newFixedThreadPool(elfs.size());

        for (var elf : elfs) {

            try {
                exec.execute(() -> this.dumpELFTrace(elf));
                TimeUnit.SECONDS.sleep(1);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            exec.shutdown();
            exec.awaitTermination(1, TimeUnit.HOURS);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
