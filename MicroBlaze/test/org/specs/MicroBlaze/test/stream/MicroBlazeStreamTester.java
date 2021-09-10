package org.specs.MicroBlaze.test.stream;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;

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

        /*
        var elf = MicroBlazeLivermoreN100.matmul;
        
        var app = elf.toApplication();
        var istream = elf.toTraceStream();
        
        var s = app.get(Application.GCC);
        
        istream.getApp().*/

        printStream(MicroBlazeLivermoreN100.matmul.toStaticStream());
    }

    @Test
    public void testStaticRaw() {
        rawDump(MicroBlazeLivermoreN100.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRawFromTxtDump() {
        rawDump(MicroBlazeLivermoreN100.innerprod.asTxtDump().toStaticStream());
    }

    @Test
    public void testTrace() {
        printStream(MicroBlazeLivermoreN100.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRaw() {
        rawDump(MicroBlazeLivermoreN100.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRawFromTxtDump() {

        /*
        var elf = MicroBlazeLivermoreN100.matmul.asTraceTxtDump();
        
        var app = elf.toApplication();
        var istream = elf.toTraceStream();
        
        var s = app.get(Application.GCC);*/

        rawDump(MicroBlazeLivermoreN100.innerprod.asTraceTxtDump().toTraceStream());
    }
}
