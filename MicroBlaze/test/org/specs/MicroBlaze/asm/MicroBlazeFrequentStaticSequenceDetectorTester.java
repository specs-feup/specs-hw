package org.specs.MicroBlaze.asm;

import java.io.File;
import java.util.List;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentStaticSequenceDetector;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeFrequentStaticSequenceDetectorTester {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/dump.txt");
        fd.deleteOnExit();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            var bbd = new FrequentStaticSequenceDetector(el);
            List<BinarySegment> bblist = bbd.detectSegments();

            for (BinarySegment bs : bblist) {
                bs.printSegment();
                System.out.print("\n");
            }
        }
        return;
    }
}
