package org.specs.MicroBlaze.asm;

import java.io.File;
import java.util.List;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.loopdetector.StaticBasicBlockDetector;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeStaticBasicBlockDetectorTester {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/matmul_n4096_l1000.elf");
        fd.deleteOnExit();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            var bbd = new StaticBasicBlockDetector(el);
            List<BinarySegment> bblist = bbd.detectSegments();

            for (BinarySegment bs : bblist) {
                System.out.print("Basic Block: \n");
                for (Instruction is : bs.getInstructions()) {
                    is.printInstruction();
                }
                System.out.print("\n\n");
            }
        }
        return;
    }
}
