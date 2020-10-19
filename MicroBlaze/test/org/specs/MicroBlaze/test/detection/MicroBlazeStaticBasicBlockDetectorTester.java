package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

public class MicroBlazeStaticBasicBlockDetectorTester {

    @Test
    public void testStatic() {
        var bundle = SegmentDetectTestUtils.detect("org/specs/MicroBlaze/asm/matmul.elf",
                MicroBlazeElfStream.class, StaticBasicBlockDetector.class);
        SegmentDetectTestUtils.printBundle(bundle);
    }

    @Test
    public void testTrace() {
        var bundle = SegmentDetectTestUtils.detect("org/specs/MicroBlaze/asm/matmul.elf",
                MicroBlazeTraceStream.class, TraceBasicBlockDetector.class);
        SegmentDetectTestUtils.printBundle(bundle);
    }
}
