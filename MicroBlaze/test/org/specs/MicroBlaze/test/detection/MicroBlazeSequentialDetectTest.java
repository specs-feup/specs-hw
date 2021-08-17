package org.specs.MicroBlaze.test.detection;

import java.util.Arrays;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN10;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FixedSizeMegablockDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

public class MicroBlazeSequentialDetectTest {

    private void testSequentialDetectors(MicroBlazeELFProvider elf) {

        // file
        int minwindow = 10, maxwindow = 10;

        // do all detectors sequentially
        for (int i = minwindow; i <= maxwindow; i++) {

            var istream1 = elf.toTraceStream();
            istream1.silent(false);

            var startAddr = istream1.getApp().getKernelStart();
            var stopAddr = istream1.getApp().getKernelStop();

            istream1.advanceTo(startAddr);

            System.out.println("Looking for segments of size: " + i);

            var detector1 = new FixedSizeMegablockDetector( // new TraceBasicBlockDetector(//
                    new DetectorConfigurationBuilder()
                            .withMaxWindow(i)
                            .withStartAddr(startAddr)
                            .withStopAddr(stopAddr)
                            .withPrematureStopAddr(stopAddr)
                            .build());
            var result1 = detector1.detectSegments(istream1);
            if (result1.getSegments().size() == 0)
                continue;

            SegmentDetectTestUtils.printBundle(result1);
        }
    }

    @Test
    public void testSequentialDetectors() {
        for (var file : Arrays.asList(MicroBlazeLivermoreN10.pic2d)) {
            this.testSequentialDetectors(file);
        }
    }
}
