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
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProducer;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction.CustomInstructionUnitGenerator;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;

public class MicroBlazeCustomInstructionUnitGeneratorTester {

    @Test
    public void findGoodSequence() {

        // get static frequent sequence bundle
        var bunds = ThreadedSegmentDetectUtils.getSegments(
                MicroBlazeLivermoreELFN100.innerprod100, 2, 4,
                MicroBlazeTraceProducer.class,
                MicroBlazeTraceStream.class,
                FrequentTraceSequenceDetector.class);

        // get longest
        SegmentBundle chosen = null;
        var it = bunds.listIterator(bunds.size());
        while (it.hasPrevious()) {
            chosen = it.previous();
            if (chosen.getSegments().size() > 0)
                break;
        }

        // transform into graph bundle
        var graphs = GraphBundle.newInstance(chosen);

        // generate custom instruction unit for one of the graphs
        var customInstGen = new CustomInstructionUnitGenerator(true);

        // ONE graph
        var graph = graphs.getGraphs().get(0);
        System.out.println(graph.getSegment().getRepresentation());

        // generate the verilog
        var unit = customInstGen.generateHardware(graph);

        // emit code for first graph with non-zero number of liveouts and liveins
        // graph.generateOutput();
        unit.emit(System.out);
    }

    @Test
    public void testCustomUnitGenerate() {

        // get static frequent sequence bundle
        var bundle = SegmentDetectTestUtils.detect(MicroBlazeLivermoreELFN100.cholesky100,
                MicroBlazeElfStream.class, FrequentStaticSequenceDetector.class,
                (new DetectorConfigurationBuilder()
                        .withMaxWindow(5)
                        .withStartAddr(MicroBlazeLivermoreELFN100.cholesky100.getKernelStart())
                        .withStopAddr(MicroBlazeLivermoreELFN100.cholesky100.getKernelStop()))
                                .build());

        // transform into graph bundle
        var graphs = GraphBundle.newInstance(bundle);

        // generate custom instruction unit for one of the graphs
        var customInstGen = new CustomInstructionUnitGenerator(true);

        // get ONE graph
        /* var graph = graphs
                .getGraphs(data -> data.getCpl() > 2 && data.getMaxwidth() > 1 && data.getLiveins().size() > 0
                        && data.getLiveouts().size() > 0)
                // && data.getNumLoads() == 0 && data.getNumStores() == 0)
                .get(0);*/

        var graph = graphs
                .getGraphs(data -> data.getSegment().getSegmentLength() == 5).get(0);

        // ONE graph
        System.out.println(graph.getSegment().getRepresentation());

        // generate the verilog
        var unit = customInstGen.generateHardware(graph);

        // emit code for first graph with non-zero number of liveouts and liveins
        // graph.generateOutput();
        unit.emit(System.out);
    }
}
