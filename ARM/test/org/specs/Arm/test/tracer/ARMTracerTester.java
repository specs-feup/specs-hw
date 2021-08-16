package org.specs.Arm.test.tracer;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN10;
import org.specs.Arm.provider.ArmLivermoreN100;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.test.tracer.TraceGraphTestUtils;

public class ARMTracerTester {

    @Test
    public void testTraceGraphingAllELFs() {
        /*for (var file : ArmLivermoreELFN10.values()) {
            TraceGraphTestUtils.testTraceStreamGraph(file, ArmTraceStream.class);
        }*/
        TraceGraphTestUtils.testTraceStreamGraph(
                ArmLivermoreN100.hydro, ArmTraceStream.class);
    }

    /*
     * Get BasicBlockTraceUnit's from trace stream 
     */
    @Test
    public void testBasicBlockTraceUnit() {
        TraceGraphTestUtils.testBasicBlockTraceUnit(
                ArmLivermoreN10.innerprod, ArmTraceStream.class);
    }
}
