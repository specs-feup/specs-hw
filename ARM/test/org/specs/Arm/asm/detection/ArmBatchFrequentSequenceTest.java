package org.specs.Arm.asm.detection;

import org.junit.Test;
import org.specs.Arm.ArmLivermoreELFN100;
import org.specs.Arm.stream.ArmTraceProducer;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;

public class ArmBatchFrequentSequenceTest {

    /*
     * Stats from static frequent sequences
     
    @Test
    public void ArmFrequentSequenceDetect() {
        ELFProvider elfs[] = ArmLivermoreELFN10.values();
        // ELFProvider elfs[] = { ArmLivermoreELFN10.innerprod100 };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 2, 20,
                ArmStaticProvider.class,
                ArmElfStream.class,
                FrequentStaticSequenceDetector.class);
    }
    */
    /*
     * Stats from trace basic blocks
     */
    @Test
    public void ArmTraceBasicBlockDetect() {

        // using the N10 version is fine, if we use the kernel start and stop bounds

        // ELFProvider elfs[] = ArmLivermoreELFN10.values();
        ELFProvider elfs[] = { ArmLivermoreELFN100.tri_diag };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 4, 50,
                ArmTraceProducer.class,
                ArmTraceStream.class,
                TraceBasicBlockDetector.class);
    }
}
