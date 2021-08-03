package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchSmallFloat;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeStaticProducer;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProducer;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;

public class MicroBlazeBatchDetectTest {

    /*
     * 
     */
    @Test
    public void MicroBlazeFrequentSequenceDetect() {
        var elf = MicroBlazeLivermoreELFN100.innerprod100;
        var bundles = ThreadedSegmentDetectUtils.getSegments(
                elf, 2, 10,
                MicroBlazeTraceProducer.class,
                MicroBlazeTraceStream.class,
                TraceBasicBlockDetector.class);

        for (var bund : bundles)
            for (var seg : bund.getSegments())
                seg.printSegment();
    }

    /*
     * Stats from static frequent sequences
     */
    @Test
    public void doMicroBlazeFrequentSequenceStats() {
        // (new HeapWindow()).run();

        // ELFProvider elfs[] = MicroBlazeLivermoreELFN100.values();
        // ELFProvider elfs[] = MicroBlazeLivermoreELFN100.values();
        // ELFProvider elfs[] = { MicroBlazeLivermoreELFN100.innerprod100 };
        ELFProvider elfs[] = MicroBlazePolyBenchSmallFloat.values();
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 2, 20,
                MicroBlazeStaticProducer.class,
                MicroBlazeElfStream.class,
                FrequentStaticSequenceDetector.class);
    }

    /*
     * Stats from static basic blocks
     */
    @Test
    public void doMicroBlazeStaticBasicBlockStats() {

        // (new HeapWindow()).run();

        // ELFProvider elfs[] = MicroBlazeLivermoreELFN100.values();
        // ELFProvider elfs[] = { MicroBlazeLivermoreELFN100.innerprod100 };
        ELFProvider elfs[] = MicroBlazePolyBenchSmallFloat.values();
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 2, 50,
                MicroBlazeStaticProducer.class,
                MicroBlazeElfStream.class,
                StaticBasicBlockDetector.class);
    }

    /*
     * Stats from trace basic blocks
     */
    @Test
    public void doMicroBlazeTraceBasicBlockStats() {

        // (new HeapWindow()).run();
        // (new MemoryProfiler()).execute();
        // ELFProvider elfs[] = MicroBlazeLivermoreELFN100.values();

        // ELFProvider elfs[] = MicroBlazePolyBenchBLAS.values(); // MINI

        // ELFProvider elfs[] = MicroBlazePolyBenchBLASLarge.values();

        ELFProvider elfs[] = MicroBlazePolyBenchSmallFloat.values();
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 7, 20,
                MicroBlazeTraceProducer.class,
                MicroBlazeTraceStream.class,
                TraceBasicBlockDetector.class);
    }
}
