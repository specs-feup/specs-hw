package org.specs.MicroBlaze.test.processes;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeQEMU;
import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.asm.MicroBlazeELFDump;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchSmallInt;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.processes.GDBRun;

public class MicroBlazeGDBRunTester {

    /*
     * set confirm off
        undisplay
        set print address off
        set height 0
        file <ELFNAME>
        
        
    MB:        target remote | <QEMUBIN> -nographic -M microblaze-fdt-plnx 
    -m 64 -display none -kernel <ELFNAME> -dtb <DTBFILE> -chardev stdio,mux=on,id=char0 
    -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
    
    ARM        target remote | <QEMUBIN> -M virt -cpu cortex-a53 -nographic 
    -kernel <ELFNAME> -chardev stdio,mux=on,id=char0 
    -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
     */

    /**
     * Test register gathering
     */
    @Test
    public void testRegister() {
        var elf = MicroBlazePolyBenchSmallInt.lu;
        // var elf = MicroBlazeLivermoreELFN100.matmul_N100;
        var app = new MicroBlazeApplication(elf);
        try (var gdb = GDBRun.newInstanceInteractive(app)) {

            // launch QEMU, then GDB (on localhost:1234)
            gdb.start();

            // run until kernel start
            gdb.runUntil(elf.getKernelStart().toString());

            for (int i = 0; i < 200; i++) {
                gdb.stepi();
                var ret = gdb.getAddrAndInstruction();
                if (ret == null)
                    break;

                System.out.println(ret);
            }
        }
    }

    /**
     * Test the GDBRun class by giving it a script and consuming all output
     */
    @Test
    public void testScript() {
        var elf = MicroBlazeLivermoreELFN100.matmul_N100;
        var app = new MicroBlazeApplication(elf);
        try (var gdb = GDBRun.newInstanceFreeRun(app)) {

            String line = null;
            while ((line = gdb.receive()) != null) {
                System.out.println(line);
            }
        }
    }

    @Test
    public void test() {
        var elf = MicroBlazeLivermoreELFN100.matmul_N100;
        var app = new MicroBlazeApplication(elf);

        try (var gdb = new GDBRun(app)) {

            System.out.println(gdb.loadFile(app));

            // copy dtb to local folder
            var dtb = app.get(Application.BAREMETAL_DTB).write();

            var remoteCommand = app.get(Application.QEMUEXE)
                    + " -nographic -M microblaze-fdt-plnx -m 64 -display none"
                    + " -kernel " + app.getElffile().getAbsolutePath().replace("\\", "/")
                    + " -dtb " + dtb.getAbsolutePath().replace("\\", "/")
                    + " -chardev stdio,mux=on,id=char0"
                    + " -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S";

            System.out.println(gdb.launchTarget(remoteCommand));

            /* gdb.sendGDBCommand("while $pc != 0x80\nstepi 1\nx/x $pc\nend");
            
            String line = null;
            while ((line = gdb.getGDBResponse()) != null) {
                System.out.println(line);
            }*/

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

            // var variables = gdb.getVariableList();
            // System.out.println(variables);
        }
    }

    @Test
    public void testQEMU() {

        // try (var qemu = new MicroBlazeQEMU(new MicroBlazeApplication(MicroBlazeLivermoreELFN100.matmul_N100))) {
        try (var qemu = new MicroBlazeQEMU(new MicroBlazeApplication(MicroBlazePolyBenchSmallInt.gemm))) {
            qemu.start();
            System.out.println("waiting...");
        }
    }

    @Test
    public void testELFDump() {
        var dump = new MicroBlazeELFDump(MicroBlazeLivermoreELFN100.matmul_N100);
        System.out.println(dump.getInstruction(Long.valueOf("80", 16)));
    }
}
