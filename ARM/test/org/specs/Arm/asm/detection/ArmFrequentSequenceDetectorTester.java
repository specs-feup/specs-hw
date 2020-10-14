package org.specs.Arm.asm.detection;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

public class ArmFrequentSequenceDetectorTester {

    @Test
    public void testStatic() {
        var bundle = SegmentDetectTestUtils.detect("org/specs/Arm/asm/cholesky.elf",
                ArmElfStream.class, FrequentStaticSequenceDetector.class);
        SegmentDetectTestUtils.printBundle(bundle);
    }

    @Test
    public void testTrace() {
        var bundle = SegmentDetectTestUtils.detect("org/specs/Arm/asm/cholesky.elf",
                ArmTraceStream.class, FrequentTraceSequenceDetector.class);
        SegmentDetectTestUtils.printBundle(bundle);
    }
}
