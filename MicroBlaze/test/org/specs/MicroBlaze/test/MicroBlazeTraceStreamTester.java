package org.specs.MicroBlaze.test;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeTraceStreamTester {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/cholesky.elf");
        fd.deleteOnExit();

        try (MicroBlazeTraceStream el = new MicroBlazeTraceStream(fd)) {
            /*Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                inst.printInstruction();
            }*/
            el.rawDump();
        }
    }
}
