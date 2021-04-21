package org.specs.MicroBlaze.test.processes;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.asm.MicroBlazeApplication;

import pt.up.fe.specs.binarytranslation.processes.GDBRun;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

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
        var elf = MicroBlazeLivermoreELFN100.matmul100;
        var fd = BinaryTranslationUtils.getFile(elf);
        var app = new MicroBlazeApplication(fd);
        try (var gdb = new GDBRun(app, BinaryTranslationUtils.FillGDBScript(app))) {

            // run until kernel start
            gdb.runUntil(elf.getKernelStart().toString());

            for (int i = 0; i < 200; i++) {
                gdb.stepi();
                System.out.println(gdb.getAddrAndInstruction());
            }

            /*
            var dump = gdb.getRegisters();
            dump.prettyPrint();
            
            gdb.stepi();
            dump = gdb.getRegisters();
            dump.prettyPrint();
            */

            // check for outstanding output?
            // var output = gdb.getG
            System.out.println(gdb.killTarget());
            gdb.quit();
        }
    }

    /**
     * Test the GDBRun class by giving it a script and consuming all output
     */
    @Test
    public void testScript() {
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        var app = new MicroBlazeApplication(fd);
        try (var gdb = new GDBRun(app, BinaryTranslationUtils.FillGDBScript(app))) {

            String line = null;
            while ((line = gdb.receive()) != null) {
                System.out.println(line);
            }
        }
    }

    @Test
    public void test() {
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        var app = new MicroBlazeApplication(fd);

        try (var gdb = new GDBRun(app)) {

            System.out.println(gdb.loadFile(app));

            // copy dtb to local folder
            var dtb = BinaryTranslationUtils.getFile(app.getDtbfile().getResource());
            dtb.deleteOnExit();

            var remoteCommand = app.getQemuexe().getResource() + ".exe"
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

            // kill target and quit gdb
            gdb.quit();
        }
    }
}
