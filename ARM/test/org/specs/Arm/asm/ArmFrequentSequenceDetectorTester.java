package org.specs.Arm.asm;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentSequenceDetector;
import pt.up.fe.specs.util.SpecsIo;

public class ArmFrequentSequenceDetectorTester {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/aarch64_bare_metal_qemu/test64.elf");
        fd.deleteOnExit();

        try (ArmElfStream el = new ArmElfStream(fd)) {
            // try (ArmTraceStream el = new ArmTraceStream(fd)) {
            var bbd = new FrequentSequenceDetector(el);
            List<BinarySegment> bblist = bbd.detectSegments();

            for (BinarySegment bs : bblist) {
                bs.printSegment();
                System.out.print("\n");
            }
        }
        return;
    }

}
