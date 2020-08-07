package org.specs.Arm.asm.bundles;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;

import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.SegmentDetector;
import pt.up.fe.specs.util.SpecsIo;

public class ArmSegmentBundleTester {

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
        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/cholesky.txt");
        fd.deleteOnExit();
        try (ArmElfStream el = new ArmElfStream(fd)) {
            var bbd = new FrequentStaticSequenceDetector(el);
            bundleToFile(bbd);
        }
    }
}