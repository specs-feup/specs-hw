package org.specs.Arm.test.detection;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN10;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;

public class ArmFrequentSequenceDetectorTester {

    @Test
    public void testStatic() {
        var elfstream = ArmLivermoreN10.cholesky.toStaticStream();
        var bbdetect = new FrequentStaticSequenceDetector();
        var bundle = bbdetect.detectSegments(elfstream);
        bundle.printBundle(segment -> segment.getSegmentLength() == 4);
    }

    @Test
    public void testTrace() {
        var tracestream = ArmLivermoreN10.cholesky.toTraceStream();
        var bbdetect = new FrequentTraceSequenceDetector();
        var bundle = bbdetect.detectSegments(tracestream);
        bundle.printBundle();
    }
}
