package org.specs.Arm.asm.detection;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.binarysegments.detection.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

public class ArmStaticBasicBlockDetectorTester {

    @Test
    public void testStatic() {
        var bundle = SegmentDetectTestUtils.detect("org/specs/Arm/asm/cholesky.elf",
                ArmElfStream.class, StaticBasicBlockDetector.class);
        SegmentDetectTestUtils.printBundle(bundle);
    }

    @Test
    public void testTrace() {
        var bundle = SegmentDetectTestUtils.detect("org/specs/Arm/asm/cholesky.elf",
                ArmTraceStream.class, TraceBasicBlockDetector.class);
        SegmentDetectTestUtils.printBundle(bundle);
    }
}
