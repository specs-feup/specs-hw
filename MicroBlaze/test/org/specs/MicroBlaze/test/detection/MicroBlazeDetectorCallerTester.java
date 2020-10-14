package org.specs.MicroBlaze.test.detection;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.callers.DetectorCaller;
import pt.up.fe.specs.binarytranslation.detection.detectors.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeDetectorCallerTester {

    @Test
    public void testCallerStatic() {

        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/matmul.elf");
        fd.deleteOnExit();
        var istream = new MicroBlazeElfStream(fd);
        var detector = new FrequentStaticSequenceDetector(istream);
        var dcaller = new DetectorCaller(detector);

        Future<SegmentBundle> bundle = Executors.newSingleThreadExecutor().submit(dcaller);

        try {
            var bbundle = bundle.get();
            SegmentDetectTestUtils.printBundle(bbundle);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
