package org.specs.Arm.asm.detection;

import org.junit.Test;
import org.specs.Arm.ArmLivermoreELFN10;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

public class ArmStaticBasicBlockDetectorTester {

    @Test
    public void testStatic() {
        var bundle = SegmentDetectTestUtils.detect(ArmLivermoreELFN10.cholesky,
                ArmElfStream.class, StaticBasicBlockDetector.class);
        SegmentDetectTestUtils.printBundle(bundle, segment -> segment.getSegmentLength() == 4);
    }

    @Test
    public void testTrace() {
        var bundle = SegmentDetectTestUtils.detect(ArmLivermoreELFN10.cholesky,
                ArmTraceStream.class, TraceBasicBlockDetector.class);
        SegmentDetectTestUtils.printBundle(bundle);
    }
}
