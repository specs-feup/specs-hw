package org.specs.MicroBlaze.test.processes;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniInt;

import pt.up.fe.specs.binarytranslation.test.processes.QEMUTester;

public class MicroBlazeQEMUTester extends QEMUTester {

    /**
     * 
     */
    @Test
    public void testQEMU() {
        testQEMULaunch(MicroBlazePolyBenchMiniInt.gemm);
    }
}
