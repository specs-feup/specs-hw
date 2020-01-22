package org.specs.MicroBlaze.test;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.StaticBasicBlockDetector;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeStaticBasicBlockDetectorTester {

    // /MicroBlaze/resources/org/specs/MicroBlaze/asm/test/matmul/matmul_n4096_l1000.elf

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/matmul/matmul_n4096_l1000.elf");
        fd.deleteOnExit();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            var bbd = new StaticBasicBlockDetector(el);
            List<BinarySegment> bblist = bbd.detectSegments();

            for (BinarySegment bs : bblist) {

                System.out.print("Basic Block: \n");
                bs.printSegment();
                System.out.print("\n");
            }
        }
        return;
    }
}
