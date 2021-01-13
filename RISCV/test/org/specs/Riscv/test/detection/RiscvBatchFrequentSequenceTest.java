package org.specs.Riscv.test.detection;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.Riscv.RiscvLivermoreELFN100iam;
import org.specs.Riscv.stream.RiscvElfStream;
import org.specs.Riscv.stream.RiscvStaticProvider;
import org.specs.Riscv.stream.RiscvTraceProvider;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;

public class RiscvBatchFrequentSequenceTest {

    /*
     * Stats from static frequent sequences
     */
    @Test
    public void RiscvFrequentSequenceDetect() {
        ELFProvider elfs[] = RiscvLivermoreELFN100iam.values();
        // ELFProvider elfs[] = { RiscvLivermoreELFN100iam.innerprod100 };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 2, 20,
                RiscvStaticProvider.class,
                RiscvElfStream.class,
                FrequentStaticSequenceDetector.class);
    }

    /*
     * Stats from trace basic blocks
     */
    @Test
    public void RiscvTraceBasicBlockDetect() {
        ELFProvider elfs[] = RiscvLivermoreELFN100iam.values();
        // ELFProvider elfs[] = { RiscvLivermoreELFN100iam.innerprod100 };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 4, 50,
                RiscvTraceProvider.class,
                RiscvTraceStream.class,
                TraceBasicBlockDetector.class);
    }
}
