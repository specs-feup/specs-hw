package org.specs.Riscv.test.detection;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100im;
import org.specs.Riscv.stream.RiscvTraceProducer;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;

public class RiscvBatchFrequentSequenceTest {

    /*
     * Stats from static frequent sequences
     
    @Test
    public void RiscvFrequentSequenceDetect() {
        ELFProvider elfs[] = RiscvLivermoreELFN100iam.values();
        // ELFProvider elfs[] = { RiscvLivermoreELFN100iam.innerprod100 };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 2, 20,
                RiscvStaticProvider.class,
                RiscvElfStream.class,
                FrequentStaticSequenceDetector.class);
    }*/

    /*
     * Stats from trace basic blocks
     */
    @Test
    public void RiscvTraceBasicBlockDetect() {
        // ELFProvider elfs[] = RiscvLivermoreELFN100iam.values();
        ELFProvider elfs[] = { RiscvLivermoreN100im.pic2d };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 4, 5,
                RiscvTraceProducer.class,
                RiscvTraceStream.class,
                TraceBasicBlockDetector.class);
    }
}
