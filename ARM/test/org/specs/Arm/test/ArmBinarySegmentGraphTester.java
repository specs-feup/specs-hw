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
 
package org.specs.Arm.test;

import org.junit.Test;
import org.specs.Arm.provider.ArmELFProvider;
import org.specs.Arm.provider.ArmLivermoreN10;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class ArmBinarySegmentGraphTester {

    private ArmELFProvider getELF() {
        return ArmLivermoreN10.cholesky;
    }

    private BinarySegmentGraph convertSegmentToGraph(BinarySegment seg) {
        return BinarySegmentGraph.newInstance(seg);
    }

    private void convertBundleToGraph(SegmentBundle bund) {
        int safetycounter = 0; // to prevent lots of printing (just for testing purposes)
        for (BinarySegment seg : bund.getSegments()) {
            var graph0 = convertSegmentToGraph(seg);
            if (safetycounter < 50) {
                if (graph0.getCpl() >= 2 && graph0.getSegment().getContexts().size() >= 1) {
                    graph0.generateOutput();
                    safetycounter++;
                }
            }
        }
    }

    private void getSegments(InstructionStream el, SegmentDetector bbd) {
        var bundle = bbd.detectSegments(el);
        var graphbundle = GraphBundle.newInstance(bundle);
        graphbundle.generateOutput(data -> data.getSegment().getContexts().size() > 10);
        // System.out.println(graphbundle.getAverageIPC());
    }

    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void testStaticFrequentSequence() {

        try (ArmElfStream el = new ArmElfStream(getELF())) {
            var bbd = new FrequentStaticSequenceDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testStaticBasicBlock() {

        try (ArmElfStream el = new ArmElfStream(getELF())) {
            var bbd = new StaticBasicBlockDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testTraceFrequenceSequence() {
        try (ArmTraceStream el = new ArmTraceStream(getELF())) {
            var bbd = new FrequentTraceSequenceDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testTraceBasicBlock() {
        try (ArmTraceStream el = new ArmTraceStream(getELF())) {
            var bbd = new TraceBasicBlockDetector();
            getSegments(el, bbd);
        }
    }
}
