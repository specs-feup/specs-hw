package org.specs.MicroBlaze.asm;

import java.io.File;
import java.util.List;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.loopdetector.FrequentStaticSequenceDetector;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeFrequentStaticSequenceDetectorTest {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/dump.txt");
        fd.deleteOnExit();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            var bbd = new FrequentStaticSequenceDetector(el);
            List<BinarySegment> bblist = bbd.detectSegments();

            for (BinarySegment bs : bblist) {
                System.out.print("Frequent Sequence: \n");
                bs.printSegment();
                System.out.print("\n\n");
            }
        }
        return;
    }
}
