package org.specs.MicroBlaze.test.instruction;

import org.junit.Test;
import org.specs.MicroBlaze.asm.MicroBlazeRegister;
import org.specs.MicroBlaze.instruction.MicroBlazeRegisterDump;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;

import pt.up.fe.specs.binarytranslation.processes.GDBRun;

public class MicroBlazeRegisterDumpTest {

    private static String gdbResponse = "r0             0x0                 0\n"
            + "r1             0x1f0f0             127216\n"
            + "r2             0x6440              25664\n"
            + "r3             0x2614              9748\n"
            + "r4             0xbf8147ae          -1082046546\n"
            + "r5             0x1f144             127300\n"
            + "r6             0x73c0              29632\n"
            + "r7             0x101               257\n"
            + "r8             0x400               1024\n"
            + "r9             0x681c              26652\n"
            + "r10            0xf                 15\n"
            + "r11            0x6544              25924\n"
            + "r12            0x67f4              26612\n"
            + "r13            0x6b20              27424\n"
            + "r14            0x0                 0\n"
            + "r15            0x59d4              22996\n"
            + "r16            0x0                 0\n"
            + "r17            0x0                 0\n"
            + "r18            0x0                 0\n"
            + "r19            0x1f144             127300\n"
            + "r20            0x0                 0\n"
            + "r21            0x0                 0\n"
            + "r22            0x2                 2\n"
            + "r23            0x8                 8\n"
            + "r24            0x1                 1\n"
            + "r25            0x69bc              27068\n"
            + "r26            0x6980              27008\n"
            + "r27            0x69f8              27128\n"
            + "r28            0x0                 0\n"
            + "r29            0x0                 0\n"
            + "r30            0x0                 0\n"
            + "r31            0x0                 0\n"
            + "rpc            0x2614              9748\n"
            + "rmsr           0x0                 0\n"
            + "rear           0x0                 0\n"
            + "resr           0x0                 0\n"
            + "rfsr           0x0                 0\n"
            + "rbtr           0x0                 0\n"
            + "rpvr0          0x7f202500          2132813056\n"
            + "rpvr1          0x0                 0\n"
            + "rpvr2          0xf0017a01          -268338687\n"
            + "rpvr3          0x0                 0\n"
            + "rpvr4          0x0                 0\n"
            + "rpvr5          0x0                 0\n"
            + "rpvr6          0x0                 0\n"
            + "rpvr7          0x0                 0\n"
            + "rpvr8          0x0                 0\n"
            + "rpvr9          0x0                 0\n"
            + "rpvr10         0xc000000           201326592\n"
            + "rpvr11         0x200000            2097152\n"
            + "redr           0x0                 0\n"
            + "rpid           0x0                 0\n"
            + "rzpr           0x0                 0\n"
            + "rtlbx          0x0                 0\n"
            + "rtlbsx         0x0                 0\n"
            + "rtlblo         0x0                 0\n"
            + "rtlbhi         0x0                 0\n"
            + "rslr           0xffffffff00000000  18446744069414584320\n"
            + "rshr           0x55b9ffffffff      94257352278015";

    @Test
    public void MicroBlazeTestDump() {
        // var regDefinitions = Arrays.asList(MicroBlazeRegister.values());
        // for (var regdef : regDefinitions)
        // System.out.println(regdef.getName());

        var mbdump = new MicroBlazeRegisterDump(gdbResponse);

        var map = mbdump.getRegisterMap();
        for (var regdef : map.keySet())
            System.out.println(regdef.getName() + " = " + map.get(regdef));

        mbdump.getValue(MicroBlazeRegister.R1);
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
