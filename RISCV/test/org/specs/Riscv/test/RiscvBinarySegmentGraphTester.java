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
 
package org.specs.Riscv.test;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvELFProvider;
import org.specs.Riscv.provider.RiscvLivermoreN100im;
import org.specs.Riscv.stream.RiscvElfStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class RiscvBinarySegmentGraphTester {

    private RiscvELFProvider openFile() {
        return RiscvLivermoreN100im.cholesky;
    }

    private void getSegments(InstructionStream el, SegmentDetector bbd) {
        var bundle = bbd.detectSegments(el);
        var gbundle = GraphBundle.newInstance(bundle);

        var list = gbundle.getGraphs(data -> data.getCpl() >= 1);
        for (BinarySegmentGraph graph : list) {
            graph.generateOutput();
        }
    }

    @Test
    public void testStaticFrequentSequence() {
        try (RiscvElfStream el = new RiscvElfStream(openFile())) {
            var bbd = new FrequentStaticSequenceDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testStaticBasicBlock() {
        try (RiscvElfStream el = new RiscvElfStream(openFile())) {
            var bbd = new StaticBasicBlockDetector();
            getSegments(el, bbd);
        }
    }
}
