package org.specs.MicroBlaze.test;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeTraceStreamTester {

    @Test
    public void test() {
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/helloworld/helloworld.elf");
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/matmul/matmul_n4096_l1000.elf");
        fd.deleteOnExit();

        try (MicroBlazeTraceStream el = new MicroBlazeTraceStream(fd)) {
            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                inst.printInstruction();
            }
            // el.rawDump();
        }
    }
}
