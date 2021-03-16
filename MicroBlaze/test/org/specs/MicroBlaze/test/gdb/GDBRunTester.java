package org.specs.MicroBlaze.test.gdb;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.asm.MicroBlazeApplication;

import pt.up.fe.specs.binarytranslation.gdb.GDBRun;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class GDBRunTester {

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

    @Test
    public void test() {
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        var app = new MicroBlazeApplication(fd);

        try (var gdb = new GDBRun(app)) {

            gdb.loadFile(app);

            // copy dtb to local folder
            var dtb = BinaryTranslationUtils.getFile(app.getDtbfile().getResource());
            dtb.deleteOnExit();

            var remoteCommand = app.getQemuexe().getResource()
                    + " -nographic -M microblaze-fdt-plnx -m 64 -display none"
                    + " -kernel " + app.getElffile().getAbsolutePath()
                    + " -dtb " + dtb.getAbsolutePath()
                    + " -chardev stdio,mux=on,id=char0"
                    + " -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S";

            gdb.launchTarget(remoteCommand);
            for (int i = 0; i < 10; i++) {
                gdb.stepi();
                System.out.println(gdb.getAddrAndInstruction());
                // System.out.println(gdb.getRegisters());
            }

            // var variables = gdb.getVariableList();
            // System.out.println(variables);

            // kill target and quit gdb
            gdb.quit();
        }
    }
}
