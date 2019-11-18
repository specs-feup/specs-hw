package org.specs.MicroBlaze.stream;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class MicroBlazeTraceGetter {

    static public void getTrace(String elfname) {

        Process gdb = null;

        // 1. make the gdb script file
        try {
            String dtbfullpath = "./resources/org/specs/MicroBlaze/qemu/system.dtb";

            PrintWriter gdbinit = new PrintWriter("tmpscript.gdb");
            gdbinit.println("set confirm off");
            gdbinit.println(
                    "target remote | /media/nuno/HDD/work/projects/qemu/microblazeel-softmmu/qemu-system-microblazeel "
                            + "-M microblaze-fdt-plnx -m 64 -device loader,file=" + elfname + " -gdb stdio "
                            + "-hw-dtb " + dtbfullpath + " -display none -S");
            gdbinit.println("file " + elfname);
            gdbinit.println("break _exit");
            gdbinit.println("set height 0");
            gdbinit.println("set logging file test.log");
            gdbinit.println("set logging on");
            gdbinit.println("while $pc != 0x80");
            gdbinit.println("stepi");
            gdbinit.println("disas/r $pc,$pc+1");
            gdbinit.println("end");
            gdbinit.println("quit");
            gdbinit.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. launch processes
        try {
            var gdbpb = new ProcessBuilder(Arrays.asList("mb-gdb", "-x", "tmpscript.gdb"));
            gdb = gdbpb.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            gdb.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
