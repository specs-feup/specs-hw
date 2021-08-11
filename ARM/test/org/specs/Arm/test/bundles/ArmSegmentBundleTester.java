package org.specs.Arm.test.bundles;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreELFN10;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;

public class ArmSegmentBundleTester {

    @Test
    public void bundleStatic() {

        try (var el = ArmLivermoreELFN10.cholesky.toStaticStream()) {
            var bbd = new FrequentStaticSequenceDetector();
            // bundleToFile(bbd);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
