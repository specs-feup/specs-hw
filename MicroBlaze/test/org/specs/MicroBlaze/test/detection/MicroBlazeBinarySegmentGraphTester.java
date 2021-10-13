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
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN10;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class MicroBlazeBinarySegmentGraphTester {

    private MicroBlazeELFProvider getELF() {
        return MicroBlazeLivermoreN10.cholesky;
    }

    private void getSegments(InstructionStream el, SegmentDetector bbd) {
        var bundle = bbd.detectSegments(el);
        var gbundle = GraphBundle.newInstance(bundle);

        gbundle.generateOutput(data -> data.getCpl() == 3);

        /*
        var list = gbundle
                .getGraphs(data -> data.getCpl() == 3);
        for (BinarySegmentGraph graph : list) {
            graph.generateOutput();
        }*/
    }

    @Test
    public void testStaticFrequentSequence() {
        try (var el = new MicroBlazeElfStream(getELF())) {
            var bbd = new FrequentStaticSequenceDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testStaticBasicBlock() {
        try (var el = new MicroBlazeElfStream(getELF())) {
            var bbd = new StaticBasicBlockDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testTraceFrequenceSequence() {
        try (var el = new MicroBlazeTraceStream(getELF())) {
            var bbd = new FrequentTraceSequenceDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testTraceBasicBlock() {
        try (var el = new MicroBlazeTraceStream(getELF())) {
            var bbd = new TraceBasicBlockDetector();
            getSegments(el, bbd);
        }
    }
}
