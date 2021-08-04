package pt.up.fe.specs.binarytranslation.utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import pt.up.fe.specs.binarytranslation.ELFProvider;
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
        var from = elf.getKernelStart().longValue();
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
