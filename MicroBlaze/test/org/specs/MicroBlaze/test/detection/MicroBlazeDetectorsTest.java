package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FixedSizeMegablockDetectorV2;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
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
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxWindow(6);

        var bundle = SegmentDetectTestUtils.detect("org/specs/MicroBlaze/asm/cholesky.txt",
                MicroBlazeElfStream.class, FrequentStaticSequenceDetector.class, builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * Trace Frequent Sequence
     */
    @Test
    public void testFrequentTraceSequenceDetector() {
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxWindow(10);

        var bundle = SegmentDetectTestUtils.detect("org/specs/MicroBlaze/asm/cholesky_trace.txt",
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

        var bundle = SegmentDetectTestUtils.detect("org/specs/MicroBlaze/asm/cholesky.txt",
                MicroBlazeElfStream.class, StaticBasicBlockDetector.class, builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * Trace Basic Block
     */
    @Test
    public void testTraceBasicBlockDetector() {
        var bundle = SegmentDetectTestUtils.detect("org/specs/MicroBlaze/asm/cholesky_trace.txt",
                MicroBlazeTraceStream.class, TraceBasicBlockDetector.class);
        SegmentDetectTestUtils.printBundle(bundle);
    }

    /*
     * Megablock
     */
    @Test
    public void testMegablock() {
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxBlocks(10).withMaxWindow(1000);

        var bundle = SegmentDetectTestUtils.detect("org/specs/MicroBlaze/asm/matmul.txt",
                MicroBlazeTraceStream.class, FixedSizeMegablockDetectorV2.class, builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }
}
