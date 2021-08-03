package org.specs.Arm.asm.tracer;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreELFN10;
import org.specs.Arm.provider.ArmLivermoreELFN100;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.test.tracer.TraceGraphTestUtils;

public class ARMTracerTester {

    @Test
    public void testTraceGraphingAllELFs() {
        /*for (var file : ArmLivermoreELFN10.values()) {
            TraceGraphTestUtils.testTraceStreamGraph(file, ArmTraceStream.class);
        }*/
        TraceGraphTestUtils.testTraceStreamGraph(
                ArmLivermoreELFN100.hydro, ArmTraceStream.class);
    }

    /*
     * Get BasicBlockTraceUnit's from trace stream 
     */
    @Test
    public void testBasicBlockTraceUnit() {
        TraceGraphTestUtils.testBasicBlockTraceUnit(
                ArmLivermoreELFN10.innerprod, ArmTraceStream.class);
    }
}
