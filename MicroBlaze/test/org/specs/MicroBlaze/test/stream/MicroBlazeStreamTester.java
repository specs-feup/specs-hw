package org.specs.MicroBlaze.test.stream;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTestUtils;

public class MicroBlazeStreamTester {

    @Test
    public void testStatic() {
        InstructionStreamTestUtils.printStream("org/specs/MicroBlaze/asm/cholesky.txt", MicroBlazeElfStream.class);
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTestUtils.rawDump("org/specs/MicroBlaze/asm/cholesky.txt", MicroBlazeElfStream.class);
    }

    @Test
    public void testTrace() {
        InstructionStreamTestUtils.printStream("org/specs/MicroBlaze/asm/cholesky.elf", MicroBlazeTraceStream.class);
    }

    @Test
    public void testTraceRaw() {
        InstructionStreamTestUtils.rawDump("org/specs/MicroBlaze/asm/cholesky.elf", MicroBlazeTraceStream.class);
    }
}
