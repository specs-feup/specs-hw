package org.specs.MicroBlaze.test.gdb;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.asm.MicroBlazeApplication;

import pt.up.fe.specs.binarytranslation.gdb.GDBRun;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class GDBRunTester {

    @Test
    public void test() {
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        var app = new MicroBlazeApplication(fd);

        try (var gdb = new GDBRun(app)) {

            String line = null;
            while ((line = gdb.getGDBResponse()) != null) {
                System.out.println(line);
            }

            gdb.setupForTracing();
            while ((line = gdb.getGDBResponse()) != null) {
                System.out.println(line);
            }

            gdb.sendGDBCommand("quit");
            gdb.close();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
