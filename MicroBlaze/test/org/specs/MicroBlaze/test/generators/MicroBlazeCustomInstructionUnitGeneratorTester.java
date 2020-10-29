/**
 * Copyright 2020 SPeCS.
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

package org.specs.MicroBlaze.test.generators;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction.CustomInstructionUnitGenerator;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

public class MicroBlazeCustomInstructionUnitGeneratorTester {

    @Test
    public void testCustomUnitGenerate() {

        // get static frequent sequence bundle
        var bundle = SegmentDetectTestUtils.detect("org/specs/MicroBlaze/asm/matmul.elf",
                MicroBlazeElfStream.class, FrequentStaticSequenceDetector.class);

        // transform into graph bundle
        var graphs = GraphBundle.newInstance(bundle);

        // generate custom instruction unit for one of the graphs
        var customInstGen = new CustomInstructionUnitGenerator(true);

        // get ONE graph
        var graph = graphs
                .getGraphs(data -> data.getCpl() > 2 && data.getMaxwidth() > 1 && data.getLiveins().size() > 0
                        && data.getLiveouts().size() > 0)
                // && data.getNumLoads() == 0 && data.getNumStores() == 0)
                .get(1);

        // System.out.println(graph.getSegment().getRepresentation());

        // generate the verilog
        var unit = customInstGen.generateHardware(graph);

        // emit code for first graph with non-zero number of liveouts and liveins
        // graph.generateOutput();
        unit.emit(System.out);
    }
}
