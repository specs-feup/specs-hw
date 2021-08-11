package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN10;
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
        var app = MicroBlazeLivermoreELFN10.cholesky.toApplication();
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxWindow(3);

        var bundle = SegmentDetectTestUtils.detect(app,
                MicroBlazeElfStream.class,
                FrequentStaticSequenceDetector.class,
                builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * Trace Frequent Sequence
     */
    @Test
    public void testFrequentTraceSequenceDetector() {
        var app = MicroBlazeLivermoreELFN10.cholesky.toApplication();
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxWindow(10);
        builder.withSkipToAddr(app.getKernelStart());
        builder.withPrematureStopAddr(app.getKernelStop());

        // TODO ensure trace is called here
        var bundle = SegmentDetectTestUtils.detect(app,
                MicroBlazeTraceStream.class,
                FrequentTraceSequenceDetector.class,
                builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * Static Basic Block
     */
    @Test
    public void testStaticBasicBlockDetector() {
        var app = MicroBlazeLivermoreELFN10.cholesky.toApplication();
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxWindow(6);

        var bundle = SegmentDetectTestUtils.detect(app,
                MicroBlazeElfStream.class,
                StaticBasicBlockDetector.class,
                builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * Trace Basic Block
     */
    @Test
    public void testTraceBasicBlockDetector() {

        var app = MicroBlazeLivermoreELFN10.cholesky.toApplication();
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxWindow(12)
                .withStartAddr(app.getKernelStart())
                .withStopAddr(app.getKernelStop())
                .withPrematureStopAddr(app.getKernelStop())
                .withSkipToAddr(app.getKernelStart());

        // TODO ensure trace is called here
        var bundle = SegmentDetectTestUtils.detect(app,
                MicroBlazeTraceStream.class,
                TraceBasicBlockDetector.class,
                builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * Megablock
     */
    @Test
    public void testMegablock() {
        var app = MicroBlazeLivermoreELFN10.matmul.toApplication();
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxBlocks(10)
                .withMaxWindow(1000)
                .withStartAddr(app.getKernelStart())
                .withStopAddr(app.getKernelStop());

        var bundle = SegmentDetectTestUtils.detect(app,
                MicroBlazeTraceStream.class,
                FixedSizeMegablockDetector.class,
                builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * v3 tester
     */
    @Test
    public void testGenericTraceDetector() {
        var elf = MicroBlazeLivermoreELFN10.matmul;
        try (var tstream = new MicroBlazeTraceStream(elf)) {
            var detector = new GenericTraceSegmentDetector(tstream);
            detector.detectSegments();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
