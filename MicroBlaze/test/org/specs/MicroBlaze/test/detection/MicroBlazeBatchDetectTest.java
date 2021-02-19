package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeStaticProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;
import pt.up.fe.specs.util.utilities.heapwindow.HeapWindow;

public class MicroBlazeBatchDetectTest {

    /*
     * Stats from static frequent sequences
     */
    @Test
    public void MicroBlazeFrequentSequenceDetect() {

        (new HeapWindow()).run();

        ELFProvider elfs[] = MicroBlazeLivermoreELFN100.values();
        // ELFProvider elfs[] = { MicroBlazeLivermoreELFN100.innerprod100 };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 2, 20,
                MicroBlazeStaticProvider.class,
                MicroBlazeElfStream.class,
                FrequentStaticSequenceDetector.class);
    }

    /*
     * Stats from static basic blocks
     */
    @Test
    public void MicroBlazeStaticBasicBlockDetect() {

        (new HeapWindow()).run();

        ELFProvider elfs[] = MicroBlazeLivermoreELFN100.values();
        // ELFProvider elfs[] = { MicroBlazeLivermoreELFN100.innerprod100 };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 2, 50,
                MicroBlazeStaticProvider.class,
                MicroBlazeElfStream.class,
                StaticBasicBlockDetector.class);
    }

    /*
     * Stats from trace basic blocks
     */
    @Test
    public void MicroBlazeTraceBasicBlockDetect() {

        // (new HeapWindow()).run();
        // (new MemoryProfiler()).execute();

        // ELFProvider elfs[] = MicroBlazeLivermoreELFN10.values();
        ELFProvider elfs[] = { MicroBlazeLivermoreELFN100.tri_diag100 };
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 2, 50,
                MicroBlazeTraceProvider.class,
                MicroBlazeTraceStream.class,
                TraceBasicBlockDetector.class);
    }
}
