package org.specs.MicroBlaze.test;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeTraceStreamTester {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/helloworld/hello.elf");
        fd.deleteOnExit();

        try (MicroBlazeTraceStream el = new MicroBlazeTraceStream(fd)) {

            while (el.nextInstruction() != null)
                ;

            /*
            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                inst.printInstruction();
            }*/
        }
    }
}
