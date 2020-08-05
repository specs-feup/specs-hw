package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.binarysegments.detection.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

public class MicroBlazeStaticBasicBlockDetectorTester {

    @Test
    public void testStatic() {
        SegmentDetectTestUtils.detect("/org/specs/MicroBlaze/asm/matmul.elf",
                MicroBlazeElfStream.class, StaticBasicBlockDetector.class);
    }

    @Test
    public void testTrace() {
        SegmentDetectTestUtils.detect("org/specs/MicroBlaze/asm/matmul.elf",
                MicroBlazeTraceStream.class, TraceBasicBlockDetector.class);
    }
}
