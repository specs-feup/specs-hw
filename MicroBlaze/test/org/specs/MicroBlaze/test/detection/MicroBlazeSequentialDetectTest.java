package org.specs.MicroBlaze.test.detection;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeSequentialDetectTest {

    private void testSequentialDetectors(ELFProvider elf) {

        // file
        File fd = SpecsIo.resourceCopy(elf.getResource());
        fd.deleteOnExit();

        int minwindow = 3, maxwindow = 3;

        // do all detectors sequentially
        for (int i = minwindow; i <= maxwindow; i++) {
            var istream1 = new MicroBlazeTraceStream(fd);
            var detector1 = new FrequentTraceSequenceDetector(
                    new DetectorConfigurationBuilder()
                            .withMaxWindow(i)
                            .withStartAddr(elf.getKernelStart())
                            .withStopAddr(elf.getKernelStop())
                            .build());
            var result1 = detector1.detectSegments(istream1);
            if (result1.getSegments().size() == 0)
                continue;

            var gbundle = GraphBundle.newInstance(result1);
            gbundle.generateOutput();
            // SegmentDetectTestUtils.printBundle(result1);
        }
    }

    @Test
    public void testSequentialDetectors() {
        // for (var file : MicroBlazeLivermoreELFN100.values()) {
        for (var file : Arrays.asList(MicroBlazeLivermoreELFN100.innerprod100)) {
            this.testSequentialDetectors(file);
        }
    }
}
