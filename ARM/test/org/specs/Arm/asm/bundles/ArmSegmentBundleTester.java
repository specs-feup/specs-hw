package org.specs.Arm.asm.bundles;

import java.io.File;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.util.SpecsIo;

public class ArmSegmentBundleTester {

    @Test
    public void bundleStatic() {
        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/cholesky.txt");
        fd.deleteOnExit();
        try (ArmElfStream el = new ArmElfStream(fd)) {
            var bbd = new FrequentStaticSequenceDetector();
            // bundleToFile(bbd);
        }
    }
}
