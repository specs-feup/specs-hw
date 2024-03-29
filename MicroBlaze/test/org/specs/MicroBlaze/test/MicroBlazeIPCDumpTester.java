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
 
package org.specs.MicroBlaze.test;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN10;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;

public class MicroBlazeIPCDumpTester {

    /*
     * Dump out the IPC for all detected segments
     */
    private void dumpIPC(SegmentBundle bundle) {
        for (BinarySegment seg : bundle.getSegments()) {
            var graph0 = BinarySegmentGraph.newInstance(seg);
            /* System.out.println(seg.getContexts().size() + "\t" + graph0.getEstimatedIPC() + "\t"
                    + seg.getSegmentLength() + "\t" + graph0.getCpl());*/

            graph0.generateOutput();
        }
    }

    @Test
    public void dumpIPCAllFiles() {

        for (var elf : MicroBlazeLivermoreN10.values()) {

            // try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            // var bbd = new FrequentStaticSequenceDetector(el);
            // var bbd = new StaticBasicBlockDetector(el);

            try (var el = new MicroBlazeTraceStream(elf)) {

                // var bbd = new FrequentTraceSequenceDetector(el);
                var bbd = new TraceBasicBlockDetector();
                var bundle = bbd.detectSegments(el);
                System.out.println(elf.getFilename());
                dumpIPC(bundle);
            }
        }
    }
}
