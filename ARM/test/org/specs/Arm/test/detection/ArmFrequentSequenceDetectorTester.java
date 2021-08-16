package org.specs.Arm.test.detection;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN10;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

public class ArmFrequentSequenceDetectorTester {

    @Test
    public void testStatic() {
        var bundle = SegmentDetectTestUtils.detect(ArmLivermoreN10.cholesky.toApplication(),
                ArmElfStream.class, FrequentStaticSequenceDetector.class);
        SegmentDetectTestUtils.printBundle(bundle);
    }

    @Test
    public void testTrace() {
        var bundle = SegmentDetectTestUtils.detect(ArmLivermoreN10.cholesky.toApplication(),
                ArmTraceStream.class, FrequentTraceSequenceDetector.class);
        SegmentDetectTestUtils.printBundle(bundle);
    }
}
