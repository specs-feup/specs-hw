package org.specs.Riscv.test.processes;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvPolyBenchMiniInt;

import pt.up.fe.specs.binarytranslation.test.processes.QEMUTester;

public class RiscvQEMUTester extends QEMUTester {

    /**
     * 
     */
    @Test
    public void testQEMU() {
        testQEMULaunch(RiscvPolyBenchMiniInt.gemm);
    }
}
