package org.specs.Arm.test.detection;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN10;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;

public class ArmStaticBasicBlockDetectorTester {

    @Test
    public void testStatic() {
        var elfstream = ArmLivermoreN10.cholesky.toStaticStream();
        var bbdetect = new StaticBasicBlockDetector();
        var bundle = bbdetect.detectSegments(elfstream);
        bundle.printBundle(segment -> segment.getSegmentLength() == 4);
    }

    @Test
    public void testTrace() {
        var tracestream = ArmLivermoreN10.cholesky.toTraceStream();
        var bbdetect = new TraceBasicBlockDetector();
        var bundle = bbdetect.detectSegments(tracestream);
        bundle.printBundle();
    }
}
