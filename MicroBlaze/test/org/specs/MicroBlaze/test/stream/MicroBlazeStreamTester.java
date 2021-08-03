package org.specs.MicroBlaze.test.stream;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchSmallFloat;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTestUtils;

/**
 * Trace printing test cases; txt files are used so that the backend tools can run on Jenkins without need for
 * installing GNU utils for the MicroBlaze architecture
 * 
 * @author nuno
 *
 */
public class MicroBlazeStreamTester {

    @Test
    public void testStatic() {
        InstructionStreamTestUtils.printStream(
                MicroBlazePolyBenchSmallFloat.gemm, MicroBlazeElfStream.class);
        // MicroBlazeLivermoreELFN10.innerprod, MicroBlazeElfStream.class);
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTestUtils.rawDump(
                MicroBlazePolyBenchSmallFloat.gemm, MicroBlazeElfStream.class);
        // MicroBlazeLivermoreELFN10.innerprod, MicroBlazeElfStream.class);
    }

    @Test
    public void testTrace() {
        InstructionStreamTestUtils.printStream(
                MicroBlazePolyBenchSmallFloat.gemm, MicroBlazeTraceStream.class);
        // MicroBlazeLivermoreELFN10.innerprod, MicroBlazeTraceStream.class);
    }

    @Test
    public void testTraceRaw() {
        InstructionStreamTestUtils.rawDump(
                MicroBlazePolyBenchSmallFloat.gemm, MicroBlazeTraceStream.class);
        // MicroBlazeLivermoreELFN10.innerprod, MicroBlazeTraceStream.class);
    }
}
