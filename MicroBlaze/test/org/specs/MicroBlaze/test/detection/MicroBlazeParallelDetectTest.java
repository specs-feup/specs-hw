package org.specs.MicroBlaze.test.detection;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.threadstream.ProducerEngine;

/**
 * Comparison runs between multiple detectors in sequence, versus threaded run; txt files are used so that the backend
 * tools can run on Jenkins without need for installing GNU utils for the MicroBlaze architecture
 * 
 * @author nuno
 *
 */
public class MicroBlazeParallelDetectTest {

    private void testParallelDetectors(String elfname) {

        // file
        File fd = SpecsIo.resourceCopy(elfname);
        fd.deleteOnExit();

        int minwindow = 2, maxwindow = 20;

        // producer
        var iproducer = new MicroBlazeTraceProvider(fd);

        // host for threads
        var streamengine = new ProducerEngine<>(iproducer, op -> op.nextInstruction(),
                cc -> new MicroBlazeElfStream(fd, cc));

        // for all window sizes
        for (int i = minwindow; i < maxwindow; i++) {

            var detector1 = new FrequentTraceSequenceDetector(
                    new DetectorConfigurationBuilder().withMaxWindow(i).build());
            streamengine.subscribe(istream -> detector1.detectSegments((InstructionStream) istream));
        }

        // launch all threads (blocking)
        streamengine.launch();

        for (var consumer : streamengine.getConsumers()) {
            var results1 = (SegmentBundle) consumer.getConsumeResult();
            SegmentDetectTestUtils.printBundle(results1);
        }

        // results1.toJSON();
        // results2.toJSON();
    }

    private void testSequentialDetectors(String elfname) {

        // file
        File fd = SpecsIo.resourceCopy(elfname);
        fd.deleteOnExit();

        int minwindow = 2, maxwindow = 20;

        // do all detectors sequentially
        for (int i = minwindow; i < maxwindow; i++) {
            var istream1 = new MicroBlazeTraceStream(fd);
            var detector1 = new FrequentTraceSequenceDetector(
                    new DetectorConfigurationBuilder().withMaxWindow(i).build());
            var result1 = detector1.detectSegments(istream1);
            SegmentDetectTestUtils.printBundle(result1);
        }
    }

    @Test
    public void testParallelDetectors() {
        this.testParallelDetectors("org/specs/MicroBlaze/asm/cholesky_trace.txt");
    }

    @Test
    public void testSequentialDetectors() {
        this.testSequentialDetectors("org/specs/MicroBlaze/asm/cholesky_trace.txt");
    }
}
