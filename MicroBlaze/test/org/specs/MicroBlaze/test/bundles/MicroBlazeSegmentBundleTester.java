package org.specs.MicroBlaze.test.bundles;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.SegmentDetector;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeSegmentBundleTester {

    private void bundleToFile(SegmentDetector bbd) {
        var bundle = bbd.detectSegments();
        try {
            bundle.serializeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bundleStatic() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/cholesky.txt");
        fd.deleteOnExit();
        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            var bbd = new FrequentStaticSequenceDetector(el);
            bundleToFile(bbd);
        }
    }
}
