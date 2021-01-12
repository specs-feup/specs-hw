package org.specs.Riscv.test.detection;

import org.junit.Test;
import org.specs.Riscv.RiscvLivermoreELFN100iam;
import org.specs.Riscv.stream.RiscvElfStream;
import org.specs.Riscv.stream.RiscvStaticProvider;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;
import pt.up.fe.specs.binarytranslation.utils.BundleStatsUtils;

public class RiscvBatchFrequentSequenceTest {

    /*
     * Stats from frequent sequences
     */
    @Test
    public void RiscvFrequentSequenceDetect() {

        for (var elf : RiscvLivermoreELFN100iam.values()) {
            // for (var elf : Arrays.asList(RiscvLivermoreELFN100iam.innerprod100)) {

            var segs = ThreadedSegmentDetectUtils.getSegments(elf, 12,
                    RiscvStaticProvider.class,
                    RiscvElfStream.class,
                    FrequentStaticSequenceDetector.class);

            BundleStatsUtils.bundleStatsDump(elf, segs);
        }
    }

}
