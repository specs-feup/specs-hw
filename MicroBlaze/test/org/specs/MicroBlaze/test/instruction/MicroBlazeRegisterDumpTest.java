package org.specs.MicroBlaze.test.instruction;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeRegisterDump;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;

import pt.up.fe.specs.binarytranslation.processes.GDBRun;

public class MicroBlazeRegisterDumpTest {

    private static String gdbResponse = "r8             0x400ad0 4197072\n"
            + "r9             0x7ffff7dea560   140737351951712\n"
            + "r10            0x7fffffffdc30   140737488346160\n"
            + "r11            0x7ffff7732dd0   140737344908752\n"
            + "r12            0x4007f0 4196336\n"
            + "r13            0x7fffffffde80   140737488346752";

    @Test
    public void MicroBlazeTestDump() {
        // var regDefinitions = Arrays.asList(MicroBlazeRegister.values());
        // for (var regdef : regDefinitions)
        // System.out.println(regdef.getName());

        var mbdump = new MicroBlazeRegisterDump(gdbResponse);

        var map = mbdump.getRegisterMap();
        for (var regdef : map.keySet())
            System.out.println(regdef.getName() + " = " + map.get(regdef));
    }

    @Test
    public void auxGetGDBResponse() {
        var app = MicroBlazeLivermoreN100.innerprod.toApplication();
        try (var gdb = GDBRun.newInstanceInteractive(app)) {

            // launch QEMU, then GDB (on localhost:1234)
            gdb.start();

            // run until kernel start
            gdb.runUntil(app.getELFProvider().getFunctionName());

            System.out.println(gdb.getRegisters());
        }
    }
}
