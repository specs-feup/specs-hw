package org.specs.Riscv.test.processes;

import java.util.Arrays;

import org.junit.Test;
import org.specs.Riscv.asm.RiscvELFDump;
import org.specs.Riscv.provider.RiscvLivermoreN100imaf;

import pt.up.fe.specs.binarytranslation.test.processes.GDBRunTester;

public class RiscvGDBRunTester extends GDBRunTester {

    /**
     * Test the GDBRun class by giving it a script and consuming all output
     */
    @Test
    public void testScript() {
        var elf = RiscvLivermoreN100imaf.matmul;
        testScript(Arrays.asList(elf));
    }

    /**
     * Test runUntil function start
     */
    @Test
    public void testRunToKernelStart() {
        var elfs = RiscvLivermoreN100imaf.values();
        testRunToKernelStart(Arrays.asList(elfs));
    }

    /**
     * 
     */
    @Test
    public void testGDBFeatures() {
        var elf = RiscvLivermoreN100imaf.matmul;
        testGDBFeatures(Arrays.asList(elf));
    }

    @Test
    public void testELFDump() {
        var dump = new RiscvELFDump(RiscvLivermoreN100imaf.matmul);
        System.out.println(dump.getInstruction(Long.valueOf("80", 16)));
    }
}
