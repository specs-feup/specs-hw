package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.MicroBlaze.MicroBlazePolyBenchBLAS;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeStaticProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;

public class MicroBlazeBatchDetectTest {

    /*
     * Stats from static frequent sequences
     */
    @Test
    public void MicroBlazeFrequentSequenceDetect() {
        // ELFProvider elfs[] = MicroBlazeLivermoreELFN100.values();
        // ELFProvider elfs[] = { MicroBlazeLivermoreELFN100.innerprod100 };
        ELFProvider elfs[] = MicroBlazePolyBenchBLAS.values();
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 2, 15,
                MicroBlazeStaticProvider.class,
                MicroBlazeElfStream.class,
                FrequentStaticSequenceDetector.class);
    }

    /*
     * Stats from trace basic blocks
     */
    @Test
    public void MicroBlazeTraceBasicBlockDetect() {
        // ELFProvider elfs[] = MicroBlazeLivermoreELFN100.values();
        // ELFProvider elfs[] = MicroBlazePolyBenchBLAS.values();
        ELFProvider elfs[] = { MicroBlazePolyBenchBLAS.symm };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 4, 20,
                MicroBlazeTraceProvider.class,
                MicroBlazeTraceStream.class,
                TraceBasicBlockDetector.class);
    }
}
