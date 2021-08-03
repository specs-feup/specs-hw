package org.specs.Riscv.test.detection;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreELFN100iam;
import org.specs.Riscv.stream.RiscvElfStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

public class RiscVDetectorsTest {

    /*
     * Static Frequent Sequence
     */
    @Test
    public void testFrequentStaticSequenceDetector() {

        var elf = RiscvLivermoreELFN100iam.pic2d100;

        var builder = new DetectorConfigurationBuilder();
        // builder.withMaxWindow(4).withStartAddr(elf.getKernelStart()).withStopAddr(elf.getKernelStop());

        var bundle = SegmentDetectTestUtils.detect(elf,
                RiscvElfStream.class,
                FrequentStaticSequenceDetector.class,
                builder.build());

        SegmentDetectTestUtils.printBundle(bundle);
    }
}
