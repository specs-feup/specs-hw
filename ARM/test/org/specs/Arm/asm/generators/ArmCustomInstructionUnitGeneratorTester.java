package org.specs.Arm.asm.generators;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;

import pt.up.fe.specs.binarytranslation.binarysegments.detection.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graphs.GraphBundle;
import pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction.CustomInstructionUnitGenerator;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

public class ArmCustomInstructionUnitGeneratorTester {

    @Test
    public void testCustomUnitGenerate() {

        // get static frequent sequence bundle
        var bundle = SegmentDetectTestUtils.detect("org/specs/Arm/asm/cholesky.elf",
                ArmElfStream.class, StaticBasicBlockDetector.class);

        // transform into graph bundle
        var graphs = GraphBundle.newInstance(bundle);

        // generate custom instruction unit for one of the graphs
        var customInstGen = new CustomInstructionUnitGenerator();

        // get ONE graph
        var subset = graphs.getGraphs(data -> data.getSegment().getSegmentLength() == 4);
        /*for (var g : subset) {
            System.out.println(g.getSegment().getContexts().get(0).getStartaddresses());
            System.out.println(g.getSegment().getRepresentation());
        }*/

        var graph = subset.get(4);

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
