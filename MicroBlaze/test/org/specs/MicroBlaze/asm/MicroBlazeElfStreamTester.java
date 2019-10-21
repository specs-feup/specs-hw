package org.specs.MicroBlaze.asm;

import java.io.File;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeElfStreamTester {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/matmul_n4096_l1000.elf");
        fd.deleteOnExit();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                inst.printInstruction();
            }
        }

    }
}
