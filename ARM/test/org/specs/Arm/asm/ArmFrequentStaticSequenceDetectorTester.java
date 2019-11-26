package org.specs.Arm.asm;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentStaticSequenceDetector;
import pt.up.fe.specs.util.SpecsIo;

public class ArmFrequentStaticSequenceDetectorTester {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/helloworld64_from_vivado_flow/helloworld64.elf");
        fd.deleteOnExit();

        try (ArmElfStream el = new ArmElfStream(fd)) {
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
