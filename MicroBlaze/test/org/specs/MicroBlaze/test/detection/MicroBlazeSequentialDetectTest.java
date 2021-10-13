package org.specs.MicroBlaze.test.detection;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN10;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;
import org.specs.MicroBlaze.provider.MicroBlazeTraceDumpProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FixedSizeMegablockDetector;

public class MicroBlazeSequentialDetectTest {

    private void testSequentialDetectors(MicroBlazeELFProvider elf) {

        // file
        int minwindow = 36, maxwindow = 36;

        // do all detectors sequentially
        for (int i = minwindow; i <= maxwindow; i++) {

            //var istream1 = elf.toTraceStream();
            var istream1 = new MicroBlazeTraceStream(new MicroBlazeTraceDumpProvider((MicroBlazeELFProvider) elf));
            istream1.silent(true);

            var startAddr = istream1.getApp().getKernelStart();
            var stopAddr = istream1.getApp().getKernelStop();

            istream1.runUntil(startAddr);

            System.out.println("Looking for segments of size: " + i);

            var detector1 = new FixedSizeMegablockDetector( // new TraceBasicBlockDetector(//
                    new DetectorConfigurationBuilder()
                            .withMaxWindow(i)
                            .withStartAddr(startAddr)
                            //.withStopAddr(stopAddr)
                            .withPrematureStopAddr(stopAddr)
                            .build());
            var result1 = detector1.detectSegments(istream1);
            if (result1.getSegments().size() == 0)
                continue;

            result1.printBundle();
        }
    }

    @Test
    public void testSequentialDetectors() {
        for (var file : List.of(MicroBlazeLivermoreN100.cholesky)) {
            System.out.println(file.getELFName());
            this.testSequentialDetectors(file);
        }
    }
}
