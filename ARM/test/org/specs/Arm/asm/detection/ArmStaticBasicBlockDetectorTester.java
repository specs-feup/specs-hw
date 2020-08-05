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
        SegmentDetectTestUtils.detect("org/specs/Arm/asm/cholesky.elf",
                ArmElfStream.class, StaticBasicBlockDetector.class);
    }

    @Test
    public void testTrace() {
        SegmentDetectTestUtils.detect("org/specs/Arm/asm/cholesky.elf",
                ArmTraceStream.class, TraceBasicBlockDetector.class);
    }
}
