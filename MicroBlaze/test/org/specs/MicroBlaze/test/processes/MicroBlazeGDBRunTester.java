package org.specs.MicroBlaze.test.processes;

import java.util.Arrays;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeQEMU;
import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.asm.MicroBlazeELFDump;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchSmallInt;

import pt.up.fe.specs.binarytranslation.test.processes.GDBRunTester;

public class MicroBlazeGDBRunTester extends GDBRunTester {

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
     * Test the GDBRun class by giving it a script and consuming all output
     */
    @Test
    public void testScript() {
        var elf = MicroBlazeLivermoreELFN100.matmul;
        testScript(Arrays.asList(elf));
    }

    /**
     * Test runUntil function start
     */
    @Test
    public void testRunToKernelStart() {
        var elfs = MicroBlazeLivermoreELFN10.values();
        testRunToKernelStart(Arrays.asList(elfs));
    }

    /**
     * 
     */
    @Test
    public void testGDBFeatures() {
        var elf = MicroBlazeLivermoreELFN100.matmul;
        testGDBFeatures(Arrays.asList(elf));
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
        var dump = new MicroBlazeELFDump(MicroBlazeLivermoreELFN100.matmul);
        System.out.println(dump.getInstruction(Long.valueOf("80", 16)));
    }
}
