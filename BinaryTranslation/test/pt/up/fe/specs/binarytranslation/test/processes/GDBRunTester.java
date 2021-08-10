package pt.up.fe.specs.binarytranslation.test.processes;

import java.util.List;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.processes.GDBRun;

public class GDBRunTester {

    /**
     * Test the GDBRun class by giving it a script and consuming all output
     */
    protected static void testScript(List<ELFProvider> elfs) {
        for (var elf : elfs) {
            var app = elf.toApplication();
            try (var gdb = GDBRun.newInstanceFreeRun(app)) {
                String line = null;
                while ((line = gdb.receive()) != null) {
                    System.out.println(line);
                }
            }
        }
    }

    /**
     * Test runUntil function start
     */
    protected static void testRunToKernelStart(List<ELFProvider> elfs) {

        for (var elf : elfs) {
            System.out.println("Testing run to end of " + elf.getELFName());
            var app = elf.toApplication();
            try (var gdb = GDBRun.newInstanceInteractive(app)) {

                // launch QEMU, then GDB (on localhost:1234)
                gdb.start();

                // run until kernel start
                gdb.runUntil(elf.getFunctionName());

                for (int i = 0; i < 10; i++) {
                    gdb.stepi();
                    var ret = gdb.getAddrAndInstruction();
                    if (ret == null)
                        break;

                    System.out.println(ret);
                }
            }
        }
    }

    /**
     * 
     */
    protected static void testGDBFeatures(List<ELFProvider> elfs) {

        for (var elf : elfs) {

            try (var gdb = GDBRun.newInstanceInteractive(elf.toApplication())) {

                // stepi
                for (int i = 0; i < 50; i++) {
                    gdb.stepi();
                    System.out.println(gdb.getAddrAndInstruction());
                }

                System.out.println(gdb.getVariableList());

                // memory dump
                for (int i = 0; i < 5; i++) {
                    System.out.println(gdb.readWord(1000 + i * 4));
                }
                System.out.println(gdb.readWord(1000, 50));

                var variables = gdb.getVariableList();
                System.out.println(variables);
            }
        }
    }
}
