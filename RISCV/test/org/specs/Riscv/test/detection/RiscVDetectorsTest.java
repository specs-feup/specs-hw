package org.specs.Riscv.test.detection;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100im;

import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTester;

public class RiscVDetectorsTest extends SegmentDetectTester {

    /*
     * Static Frequent Sequence
     */
    @Test
    public void testFrequentStaticSequenceDetector() {
        testFrequentStaticSequenceDetector(RiscvLivermoreN100im.cholesky.toStaticStream());
    }

    /*
     * Trace Frequent Sequence
     */
    @Test
    public void testFrequentTraceSequenceDetector() {
        testFrequentTraceSequenceDetector(RiscvLivermoreN100im.cholesky.toTraceStream());
    }

    /*
     * Static Basic Block
     */
    @Test
    public void testStaticBasicBlockDetector() {
        testStaticBasicBlockDetector(RiscvLivermoreN100im.cholesky.toStaticStream());
    }

    /*
     * Trace Basic Block
     */
    @Test
    public void testTraceBasicBlockDetector() {
        testTraceBasicBlockDetector(RiscvLivermoreN100im.cholesky.toTraceStream());
    }

    /*
     * GenericTraceSegment (v3 detector WIP)
     */
    @Test
    public void testGenericTraceDetector() {
        // var istream = MicroBlazePolyBenchMiniInt.floydwarshall.toTraceStream();
        // var istream = MicroBlazePolyBenchMiniInt.floydwarshall.asTraceTxtDump().toTraceStream();
        // var istream = MicroBlazePolyBenchMiniInt.gemm.asTraceTxtDump().toTraceStream();
        var istream = RiscvLivermoreN100im.innerprod.toTraceStream();
        testGenericTraceDetector(istream);
    }
}
