package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.v3.GenericTraceSegmentDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTester;

/**
 * Segment detection test cases; txt files are used so that the backend tools can run on Jenkins without need for
 * installing GNU utils for the MicroBlaze architecture
 * 
 * @author nuno
 *
 */
public class MicroBlazeDetectorsTest extends SegmentDetectTester {

    /*
     * Static Frequent Sequence
     */
    @Test
    public void testFrequentStaticSequenceDetector() {
        testFrequentStaticSequenceDetector(MicroBlazeLivermoreN100.cholesky.toStaticStream());
    }

    /*
     * Trace Frequent Sequence
     */
    @Test
    public void testFrequentTraceSequenceDetector() {
        testFrequentTraceSequenceDetector(MicroBlazeLivermoreN100.cholesky.toTraceStream());
    }

    /*
     * Static Basic Block
     */
    @Test
    public void testStaticBasicBlockDetector() {
        testStaticBasicBlockDetector(MicroBlazeLivermoreN100.cholesky.toStaticStream());
    }

    /*
     * Trace Basic Block
     */
    @Test
    public void testTraceBasicBlockDetector() {
        testTraceBasicBlockDetector(MicroBlazeLivermoreN100.cholesky.toTraceStream());
    }

    /*
     * Megablock
     
    @Test
    public void testMegablock() {
        var app = MicroBlazeLivermoreN10.matmul.toApplication();
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxBlocks(10)
                .withMaxWindow(1000)
                .withStartAddr(app.getKernelStart())
                .withStopAddr(app.getKernelStop());
    nordicsemi.com/
        var bundle = SegmentDetectTester.detect(app,
                MicroBlazeTraceStream.class,
                FixedSizeMegablockDetector.class,
                builder.build());
        SegmentDetectTester.printBundle(bundle);
    }*/

    /*
     * v3 tester
     */
    @Test
    public void testGenericTraceDetector() {
        // var app = MicroBlazePolyBenchMiniInt.floydwarshall.toApplication();
        // var app = MicroBlazePolyBenchMiniInt.floydwarshall.asTraceTxtDump().toApplication();
        // var app = MicroBlazePolyBenchMiniInt.gemm.asTraceTxtDump().toApplication();
        var app = MicroBlazeLivermoreN100.innerprod.asTraceTxtDump().toApplication();

        var builder = new DetectorConfigurationBuilder();
        builder.withSkipToAddr(app.getKernelStart());
        builder.withPrematureStopAddr(app.getKernelStop());
        builder.withStartAddr(app.getKernelStart());
        builder.withStopAddr(app.getKernelStop());
        builder.withMaxWindow(20);

        try (var tstream = new MicroBlazeTraceStream(app)) {
            var detector = new GenericTraceSegmentDetector(tstream, builder.build());
            var list = detector.detectSegments();

            for (var el : list) {
                System.out.println(el.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
