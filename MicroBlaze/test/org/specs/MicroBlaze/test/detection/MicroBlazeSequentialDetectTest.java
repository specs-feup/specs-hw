package org.specs.MicroBlaze.test.detection;

import java.util.Arrays;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FixedSizeMegablockDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeSequentialDetectTest {

    private void testSequentialDetectors(ELFProvider elf) {

        // file
        var fd = BinaryTranslationUtils.getFile(elf);

        int minwindow = 10, maxwindow = 10;

        // do all detectors sequentially
        for (int i = minwindow; i <= maxwindow; i++) {
            var istream1 = new MicroBlazeTraceStream(fd);
            istream1.silent(false);
            istream1.advanceTo(elf.getKernelStart().longValue());

            System.out.println("Looking for segments of size: " + i);

            var detector1 = new FixedSizeMegablockDetector( // new TraceBasicBlockDetector(//
                    new DetectorConfigurationBuilder()
                            .withMaxWindow(i)
                            .withStartAddr(elf.getKernelStart())
                            .withStopAddr(elf.getKernelStop())
                            .withPrematureStopAddr(elf.getKernelStop().longValue())
                            .build());
            var result1 = detector1.detectSegments(istream1);
            if (result1.getSegments().size() == 0)
                continue;

            // var gbundle = GraphBundle.newInstance(result1);
            // gbundle.generateOutput();
            SegmentDetectTestUtils.printBundle(result1);
        }
    }

    @Test
    public void testSequentialDetectors() {
        // for (var file : MicroBlazeLivermoreELFN100.values()) {
        for (var file : Arrays.asList(MicroBlazeLivermoreELFN10.pic2d)) {
            this.testSequentialDetectors(file);
        }
    }
}
