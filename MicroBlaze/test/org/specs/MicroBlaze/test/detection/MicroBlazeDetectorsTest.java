package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.MicroBlazePolyBenchSmallFloat;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FixedSizeMegablockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.v3.GenericTraceSegmentDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

/**
 * Segment detection test cases; txt files are used so that the backend tools can run on Jenkins without need for
 * installing GNU utils for the MicroBlaze architecture
 * 
 * @author nuno
 *
 */
public class MicroBlazeDetectorsTest {

    /*
     * Static Frequent Sequence
     */
    @Test
    public void testFrequentStaticSequenceDetector() {
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxWindow(3);

        var bundle = SegmentDetectTestUtils.detect(MicroBlazeLivermoreELFN10.cholesky,
                MicroBlazeElfStream.class, FrequentStaticSequenceDetector.class, builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * Trace Frequent Sequence
     */
    @Test
    public void testFrequentTraceSequenceDetector() {
        var elf = MicroBlazeLivermoreELFN10.cholesky;
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxWindow(10);
        builder.withSkipToAddr(elf.getKernelStart());
        builder.withPrematureStopAddr(elf.getKernelStop());

        // TODO ensure trace is called here
        var bundle = SegmentDetectTestUtils.detect(elf,
                MicroBlazeTraceStream.class, FrequentTraceSequenceDetector.class, builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * Static Basic Block
     */
    @Test
    public void testStaticBasicBlockDetector() {
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxWindow(6);

        var bundle = SegmentDetectTestUtils.detect(MicroBlazeLivermoreELFN10.cholesky,
                MicroBlazeElfStream.class, StaticBasicBlockDetector.class, builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * Trace Basic Block
     */
    @Test
    public void testTraceBasicBlockDetector() {

        var elf = MicroBlazePolyBenchSmallFloat.gemm;
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxWindow(12)
                .withStartAddr(elf.getKernelStart())
                .withStopAddr(elf.getKernelStop())
                .withPrematureStopAddr(elf.getKernelStop())
                .withSkipToAddr(elf.getKernelStart());

        // TODO ensure trace is called here
        var bundle = SegmentDetectTestUtils.detect(
                elf, MicroBlazeTraceStream.class, TraceBasicBlockDetector.class, builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * Megablock
     */
    @Test
    public void testMegablock() {
        var builder = new DetectorConfigurationBuilder();

        var elf = MicroBlazeLivermoreELFN10.matmul;
        builder.withMaxBlocks(10).withMaxWindow(1000).withStartAddr(elf.getKernelStart())
                .withStopAddr(elf.getKernelStop());

        var bundle = SegmentDetectTestUtils.detect(elf,
                MicroBlazeTraceStream.class, FixedSizeMegablockDetector.class, builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * v3 tester
     */
    @Test
    public void testGenericTraceDetector() {
        var elf = MicroBlazeLivermoreELFN10.matmul;
        var fd = BinaryTranslationUtils.getFile(elf);
        try (var tstream = new MicroBlazeTraceStream(fd)) {
            var detector = new GenericTraceSegmentDetector(tstream);
            detector.detectSegments();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
