/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniFloat;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeStaticProducer;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProducer;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.elf.ELFProvider;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;

public class MicroBlazeBatchDetectTest {

    /*
     * 
     */
    @Test
    public void MicroBlazeFrequentSequenceDetect() {
        var elf = MicroBlazeLivermoreN100.innerprod;
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
        ELFProvider elfs[] = MicroBlazePolyBenchMiniFloat.values();
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
        ELFProvider elfs[] = MicroBlazePolyBenchMiniFloat.values();
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

        ELFProvider elfs[] = MicroBlazePolyBenchMiniFloat.values();
        ThreadedSegmentDetectUtils.BatchDetect(elfs, 7, 20,
                MicroBlazeTraceProducer.class,
                MicroBlazeTraceStream.class,
                TraceBasicBlockDetector.class);
    }
}
