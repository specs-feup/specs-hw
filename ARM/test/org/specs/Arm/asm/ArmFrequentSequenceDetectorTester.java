package org.specs.Arm.asm;

import java.io.File;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.SegmentDetector;
import pt.up.fe.specs.util.SpecsIo;

public class ArmFrequentSequenceDetectorTester {

    public void test(SegmentDetector bbd) {

        var bundle = bbd.detectSegments();
        var segments = bundle.getSegments();

        // System.out.println("Coverage: " + bbd.getCoverage(2) + "\n");

        for (BinarySegment bs : segments) {
            bs.printSegment();
            System.out.print("\n");
        }
        return;
    }

    @Test
    public void testStatic() {
        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/aarch64_bare_metal_qemu/test64.elf");
        fd.deleteOnExit();

        try (ArmElfStream el = new ArmElfStream(fd)) {
            var bbd = new FrequentStaticSequenceDetector(el);
            test(bbd);
        }
    }

    @Test
    public void testTrace() {
        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/aarch64_bare_metal_qemu/test64.elf");
        fd.deleteOnExit();

        try (ArmTraceStream el = new ArmTraceStream(fd)) {
            var bbd = new FrequentTraceSequenceDetector(el);
            test(bbd);
        }
    }
}
