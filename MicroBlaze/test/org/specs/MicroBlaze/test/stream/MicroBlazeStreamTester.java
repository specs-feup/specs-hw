package org.specs.MicroBlaze.test.stream;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
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

//    @Test
//    public void testStatic() {
//        InstructionStreamTestUtils.printStream(
//                MicroBlazeLivermoreELFN10.innerprod.getResource(), MicroBlazeElfStream.class);
//    }
//
//    @Test
//    public void testStaticRaw() {
//        InstructionStreamTestUtils.rawDump(
//                MicroBlazeLivermoreELFN10.innerprod.getResource(), MicroBlazeElfStream.class);
//    }
//
//    @Test
//    public void testTrace() throws FileNotFoundException {
//        InstructionStreamTestUtils.printStream(
//                MicroBlazeLivermoreELFN10.innerprod.getResource(), MicroBlazeTraceStream.class);
//    }
    
    @Test
    public void testTraceToFile() throws FileNotFoundException {
        String elfname = "autocor-O2-small";
        
        String path = "resources/org/specs/MicroBlaze/traces/" + elfname + ".txt";
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(path)), true));
        InstructionStreamTestUtils.printStream(
                "org/specs/MicroBlaze/asm/" + elfname + ".elf", MicroBlazeTraceStream.class);
    }

//    @Test
//    public void testTraceRaw() {
//        InstructionStreamTestUtils.rawDump(
//                MicroBlazeLivermoreELFN10.innerprod.getResource(), MicroBlazeTraceStream.class);
//    }
}
