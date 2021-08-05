package org.specs.MicroBlaze.test.stream;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN10;
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
                new MicroBlazeElfStream(MicroBlazeLivermoreELFN10.innerprod));
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTestUtils.rawDump(
                new MicroBlazeElfStream(MicroBlazeLivermoreELFN10.innerprod));
    }

    @Test
    public void testStaticRawFromTxtDump() {
        InstructionStreamTestUtils.rawDump(
                new MicroBlazeElfStream(MicroBlazeLivermoreELFN10.innerprod.asTxtDump()));
    }

    @Test
    public void testTrace() {
        InstructionStreamTestUtils.printStream(
                new MicroBlazeTraceStream(MicroBlazeLivermoreELFN10.innerprod));
    }

    @Test
    public void testTraceRaw() {
        InstructionStreamTestUtils.rawDump(
                new MicroBlazeTraceStream(MicroBlazeLivermoreELFN10.innerprod));
    }

    @Test
    public void testTraceRawFromTxtDump() {
        InstructionStreamTestUtils.rawDump(
                new MicroBlazeTraceStream(MicroBlazeLivermoreELFN10.innerprod.asTraceTxtDump()));
    }
}
