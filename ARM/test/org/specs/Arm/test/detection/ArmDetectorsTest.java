package org.specs.Arm.test.detection;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN10;

import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTester;

public class ArmDetectorsTest extends SegmentDetectTester {

    // TODO: check if ArmLivermoreN10 and other ELFs for ARM are healhty

    /*
     * Static Frequent Sequence
     */
    @Test
    public void testFrequentStaticSequenceDetector() {
        testFrequentStaticSequenceDetector(ArmLivermoreN10.cholesky.toStaticStream());
    }

    /*
     * Trace Frequent Sequence
     */
    @Test
    public void testFrequentTraceSequenceDetector() {
        testFrequentTraceSequenceDetector(ArmLivermoreN10.cholesky.toTraceStream());
    }

    /*
     * Static Basic Block
     */
    @Test
    public void testStaticBasicBlockDetector() {
        testStaticBasicBlockDetector(ArmLivermoreN10.cholesky.toStaticStream());
    }

    /*
     * Trace Basic Block
     */
    @Test
    public void testTraceBasicBlockDetector() {
        testTraceBasicBlockDetector(ArmLivermoreN10.cholesky.toTraceStream());
    }
}