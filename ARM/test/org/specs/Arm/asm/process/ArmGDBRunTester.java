package org.specs.Arm.asm.process;

import org.junit.Test;
import org.specs.Arm.ArmApplication;
import org.specs.Arm.provider.ArmLivermoreELFN10;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.processes.GDBRun;

public class ArmGDBRunTester {

    @Test
    public void test() {
        var elf = ArmLivermoreELFN10.innerprod;
        var app = new ArmApplication(elf);

        try (var gdb = new GDBRun(app)) {

            gdb.loadFile(app);

            // copy dtb to local folder
            var dtb = app.get(Application.BAREMETAL_DTB).write();

            var remoteCommand = app.get(Application.QEMUEXE)
                    + " --nographic -M arm-generic-fdt"
                    + " -dtb " + dtb.getAbsolutePath()
                    + " -device loader,file=" + app.getElffile().getAbsolutePath()
                    + ",cpu-num=0 -device loader,addr=0xfd1a0104,data=0x8000000e,data-len=4"
                    + " -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S";

            gdb.launchTarget(remoteCommand);

            /* gdb.sendGDBCommand("while $pc != 0x80\nstepi 1\nx/x $pc\nend");
            
            String line = null;
            while ((line = gdb.getGDBResponse()) != null) {
                System.out.println(line);
            }*/

            // run until kernel start
            gdb.runUntil(elf.getKernelStart().toString());

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

            // kill target and quit gdb
            gdb.quit();
        }
    }
}
