package org.specs.Arm.test.bundles;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN10;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;

public class ArmSegmentBundleTester {

    @Test
    public void bundleStatic() {

        try (var el = ArmLivermoreN10.cholesky.toStaticStream()) {
            var bbd = new FrequentStaticSequenceDetector();
            // bundleToFile(bbd);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
