package org.specs.MicroBlaze.test.stream;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTestUtils;
import pt.up.fe.specs.util.SpecsIo;

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
                MicroBlazeLivermoreELFN10.innerprod, MicroBlazeElfStream.class);
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTestUtils.rawDump(
                MicroBlazeLivermoreELFN10.innerprod, MicroBlazeElfStream.class);
    }

    @Test
    public void testTrace() {
        InstructionStreamTestUtils.printStream(
                MicroBlazeLivermoreELFN10.innerprod, MicroBlazeTraceStream.class);
    }

    // TODO: rawDump now doesnt work if the qemu template doesnt include the loop...
    @Test
    public void testTraceRaw() {
        InstructionStreamTestUtils.rawDump(
                MicroBlazeLivermoreELFN10.innerprod, MicroBlazeTraceStream.class);
    }

    @Test
    public void test() {

        File fd = SpecsIo.resourceCopy(MicroBlazeLivermoreELFN10.innerprod.getResource());
        fd.deleteOnExit();

        try (var stream = new MicroBlazeElfStream(fd)) {
            Instruction inst = null;
            while ((inst = stream.nextInstruction()) != null) {
                var op = inst.getData().getOperands();
                for (var ope : op) {
                    System.out.println(ope.getAsmField().toString());
                }
            }
        }
    }
}
