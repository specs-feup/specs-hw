package org.specs.Arm.asm.detection;

import org.junit.Test;
import org.specs.Arm.ArmLivermoreELFN10;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

public class ArmFrequentSequenceDetectorTester {

    @Test
    public void testStatic() {
        var bundle = SegmentDetectTestUtils.detect(ArmLivermoreELFN10.cholesky,
                ArmElfStream.class, FrequentStaticSequenceDetector.class);
        SegmentDetectTestUtils.printBundle(bundle);
    }

    @Test
    public void testTrace() {
        var bundle = SegmentDetectTestUtils.detect(ArmLivermoreELFN10.cholesky,
                ArmTraceStream.class, FrequentTraceSequenceDetector.class);
        SegmentDetectTestUtils.printBundle(bundle);
    }
}
