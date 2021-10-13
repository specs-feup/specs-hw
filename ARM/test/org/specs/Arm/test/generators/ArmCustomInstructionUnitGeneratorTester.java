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
 
package org.specs.Arm.test.generators;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN100;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction.CustomInstructionUnitGenerator;

public class ArmCustomInstructionUnitGeneratorTester {

    @Test
    public void testCustomUnitGenerate() {

        // get static frequent sequence bundle
        /*var bundle = SegmentDetectTestUtils.detect(ArmLivermoreN100.innerprod.toApplication(),
                ArmTraceStream.class, TraceBasicBlockDetector.class,
                (new DetectorConfigurationBuilder().withMaxWindow(6)).build());*/

        // get static frequent sequence bundle
        var app = ArmLivermoreN100.cholesky.toApplication();

        var config = new DetectorConfigurationBuilder()
                .withMaxWindow(6)
                .withStartAddr(app.getKernelStart())
                .withStopAddr(app.getKernelStop()).build();

        var detector = new FrequentStaticSequenceDetector(config);
        var bundle = detector.detectSegments(new ArmTraceStream(app));

        // transform into graph bundle
        var graphs = GraphBundle.newInstance(bundle);

        // generate custom instruction unit for one of the graphs
        var customInstGen = new CustomInstructionUnitGenerator();

        /*
        // get ONE graph
        var subset = graphs.getGraphs(data -> data.getSegment().getSegmentLength() == 4);
        for (var g : subset) {
            System.out.println(g.getSegment().getContexts().get(0).getStartaddresses());
            System.out.println(g.getSegment().getRepresentation());
        }
        
        var graph = subset.get(4);*/

        var graph = graphs.getGraphs().get(0);

        for (var i : graph.getSegment().getInstructions()) {
            System.out.println(i.getRepresentation());
        }

        /*var subset = graphs.getGraphs(data -> data.getCpl() > 2
                && data.getMaxwidth() > 1
                && data.getLiveins().size() > 0
                && data.getLiveouts().size() > 0
                && data.getNumLoads() == 0
                && data.getNumStores() == 0);*/

        /*
        var graph = graphs
                .getGraphs(data -> data.getCpl() > 2
                        && data.getMaxwidth() > 1
                        && data.getLiveins().size() > 0
                        && data.getLiveouts().size() > 0
                        && data.getNumLoads() == 0
                        && data.getNumStores() == 0)
                .get(0);
        
        System.out.println(graph.getSegment().getRepresentation());*/

        // generate the verilog
        var unit = customInstGen.generateHardware(graph);

        // emit code for first graph with non-zero number of liveouts and liveins
        // graph.generateOutput();
        unit.emit(System.out);
    }
}
