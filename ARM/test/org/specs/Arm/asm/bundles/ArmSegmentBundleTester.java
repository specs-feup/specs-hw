package org.specs.Arm.asm.bundles;

import org.junit.Test;
import org.specs.Arm.ArmLivermoreELFN10;
import org.specs.Arm.stream.ArmElfStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;

public class ArmSegmentBundleTester {

    @Test
    public void bundleStatic() {
        try (ArmElfStream el = new ArmElfStream(ArmLivermoreELFN10.cholesky)) {
            var bbd = new FrequentStaticSequenceDetector();
            // bundleToFile(bbd);
        }
    }
}
