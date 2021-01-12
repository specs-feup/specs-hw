package org.specs.Arm.asm.detection;

import org.junit.Test;
import org.specs.Arm.ArmLivermoreELFN100;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmStaticProvider;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;
import pt.up.fe.specs.binarytranslation.utils.BundleStatsUtils;

public class ArmBatchFrequentSequenceTest {

    /*
     * Stats from frequent sequences
     */
    @Test
    public void ArmFrequentSequenceDetect() {

        for (var elf : ArmLivermoreELFN100.values()) {
            // for (var elf : Arrays.asList(ArmLivermoreELFN100.innerprod100)) {

            var segs = ThreadedSegmentDetectUtils.getSegments(elf, 12,
                    ArmStaticProvider.class,
                    ArmElfStream.class,
                    FrequentStaticSequenceDetector.class);

            BundleStatsUtils.bundleStatsDump(elf, segs);
        }
    }
}
