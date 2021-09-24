package pt.up.fe.specs.binarytranslation.test.instruction;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;
import pt.up.fe.specs.binarytranslation.processes.GDBRun;

public class RegisterDumpTest {

    protected static void TestRegisterDump(RegisterDump dump) {

        var map = dump.getRegisterMap();
        for (var regdef : map.keySet())
            System.out.println(regdef.getName() + " = " + map.get(regdef));

        // dump.getValue(MicroBlazeRegister.R1);
    }

    protected static void GetGDBResponse(ELFProvider elf) {
        var app = elf.toApplication();
        try (var gdb = GDBRun.newInstanceInteractive(app)) {

            // launch QEMU, then GDB (on localhost:1234)
            gdb.start();

            // run until kernel start
            gdb.runUntil(app.getELFProvider().getFunctionName());

            System.out.println(gdb.getRegisters());
        }
    }
}
