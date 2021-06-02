package org.specs.MicroBlaze.test.stream;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.MicroBlazePolyBench;
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
                MicroBlazeLivermoreELFN10.innerprod.getResource(), MicroBlazeElfStream.class);
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTestUtils.rawDump(
                MicroBlazeLivermoreELFN10.innerprod.getResource(), MicroBlazeElfStream.class);
    }

    @Test
    public void testTrace() {
        InstructionStreamTestUtils.printStream(
                MicroBlazeLivermoreELFN10.innerprod.getResource(), MicroBlazeTraceStream.class);
    }

    @Test
    public void testTraceRaw() {
        InstructionStreamTestUtils.rawDump(
                MicroBlazePolyBench.twomm.getResource(), MicroBlazeTraceStream.class);
    }
}
