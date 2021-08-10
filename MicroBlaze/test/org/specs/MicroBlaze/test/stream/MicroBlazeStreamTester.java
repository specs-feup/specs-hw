package org.specs.MicroBlaze.test.stream;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN10;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTester;

/**
 * Trace printing test cases; txt files are used so that the backend tools can run on Jenkins without need for
 * installing GNU utils for the MicroBlaze architecture
 * 
 * @author nuno
 *
 */
public class MicroBlazeStreamTester extends InstructionStreamTester {

    @Test
    public void testStatic() {
        printStream(MicroBlazeLivermoreELFN10.matmul.toStaticStream());
    }

    @Test
    public void testStaticRaw() {
        rawDump(MicroBlazeLivermoreELFN10.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRawFromTxtDump() {
        rawDump(MicroBlazeLivermoreELFN10.innerprod.asTxtDump().toStaticStream());
    }

    @Test
    public void testTrace() {
        printStream(MicroBlazeLivermoreELFN10.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRaw() {
        rawDump(MicroBlazeLivermoreELFN10.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRawFromTxtDump() {
        rawDump(MicroBlazeLivermoreELFN10.innerprod.asTraceTxtDump().toTraceStream());
    }
}
