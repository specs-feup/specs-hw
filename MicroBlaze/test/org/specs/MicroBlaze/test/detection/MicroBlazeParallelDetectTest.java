package org.specs.MicroBlaze.test.detection;

import java.io.File;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.MicroBlaze.MicroBlazeGccOptimizationLevels;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
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

    private void testParallelDetectors(ELFProvider elf) {

        // file
        File fd = SpecsIo.resourceCopy(elf.getResource());
        fd.deleteOnExit();

        int minwindow = 5, maxwindow = 50;

        // producer
        var iproducer = new MicroBlazeTraceProvider(fd);
        // var iproducer = new MicroBlazeStaticProvider(fd);

        // host for threads
        var streamengine = new ProducerEngine<>(iproducer, op -> op.nextInstruction(),
                // cc -> new MicroBlazeElfStream(fd, cc));
                cc -> new MicroBlazeTraceStream(fd, cc));

        // for all window sizes
        for (int i = minwindow; i <= maxwindow; i++) {

            // var detector1 = new FrequentTraceSequenceDetector(
            // var detector1 = new FrequentStaticSequenceDetector(
            var detector1 = new TraceBasicBlockDetector(
                    elf.getKernelStart() != null ?
                    new DetectorConfigurationBuilder()
                            .withMaxWindow(i)
                            .withStartAddr(elf.getKernelStart())
                            .withStopAddr(elf.getKernelStop())
                            .build() :
                                new DetectorConfigurationBuilder()
                                .withMaxWindow(i)
                                .build()           
                            
                    );
            streamengine.subscribe(istream -> detector1.detectSegments((InstructionStream) istream));
        }

        // launch all threads (blocking)
        streamengine.launch();

        for (var consumer : streamengine.getConsumers()) {
            var result1 = (SegmentBundle) consumer.getConsumeResult();
            // SegmentDetectTestUtils.printBundle(result1);
            //System.out.println(result1.getSummary());
            SegmentDetectTestUtils.printBundle(result1);
            var gbundle = GraphBundle.newInstance(result1);
            if (gbundle.getSegments().size() != 0)
                gbundle.generateOutput();
        }

        // results1.toJSON();
        // results2.toJSON();
    }
    
    @Test
    public void testSingleParallelDetector() {
        this.testParallelDetectors(MicroBlazeGccOptimizationLevels.test2);
    }

    @Test
    public void testParallelDetectors() {
        for (var file : MicroBlazeLivermoreELFN10.values()) {
            // for (var file : Arrays.asList(MicroBlazeLivermoreELFN100.innerprod100)) {
            System.out.println("Kernel: " + file.getFilename());
            this.testParallelDetectors(file);
            System.out.println("----------------------------------------------------------");
        }
    }
}
