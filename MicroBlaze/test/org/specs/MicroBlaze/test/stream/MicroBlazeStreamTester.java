package org.specs.MicroBlaze.test.stream;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN10;

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
        printStream(MicroBlazeLivermoreN10.matmul.toStaticStream());
    }

    @Test
    public void testStaticRaw() {
        rawDump(MicroBlazeLivermoreN10.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRawFromTxtDump() {
        rawDump(MicroBlazeLivermoreN10.innerprod.asTxtDump().toStaticStream());
    }

    @Test
    public void testTrace() {
        printStream(MicroBlazeLivermoreN10.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRaw() {
        rawDump(MicroBlazeLivermoreN10.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRawFromTxtDump() {
        rawDump(MicroBlazeLivermoreN10.innerprod.asTraceTxtDump().toTraceStream());
    }
}
