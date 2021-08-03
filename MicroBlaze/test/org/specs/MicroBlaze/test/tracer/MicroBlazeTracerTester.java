package org.specs.MicroBlaze.test.tracer;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.test.tracer.TraceGraphTestUtils;

public class MicroBlazeTracerTester {

    @Test
    public void testTraceGraphingAllELFs() {
        /*for (var file : MicroBlazeLivermoreELFN100.values()) {
            TraceGraphTestUtils.testTraceStreamGraph(file, MicroBlazeTraceStream.class);
        }*/
        TraceGraphTestUtils.testTraceStreamGraph(
                MicroBlazeLivermoreELFN10.innerprod, MicroBlazeTraceStream.class);
    }

    /*
     * Get BasicBlockTraceUnit's from trace stream 
     */
    @Test
    public void testBasicBlockTraceUnit() {
        TraceGraphTestUtils.testBasicBlockTraceUnit(
                MicroBlazeLivermoreELFN10.innerprod, MicroBlazeTraceStream.class);
    }
}
