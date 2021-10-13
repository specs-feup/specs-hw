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

package pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern;

import java.util.List;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class LoopIncrementPatternAnalyzer extends APatternAnalyzer {

    public LoopIncrementPatternAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        super(stream, elf, window);
    }

    @Override
    protected APatternReport matchTemplates(List<BinarySegment> segs) {
        var report = new LoopIncrementReport();

        for (var bb : segs) {
            var insts = bb.getInstructions();
            var dfg1 = new BasicBlockDataFlowGraph(insts, 1);
            var dfg2 = new BasicBlockDataFlowGraph(insts, 2);


            // Check for type 1
            for (var i = 1; i < 32; i++) {
                var reg = "r" + i;
                var template = getType1(reg, "1");
                var match = matchGraphToTemplate(dfg2, template, true);
                
                if (match) {
                    report.addEntry(dfg1, dfg2, IncrementType.INC_TYPE_1, reg);
                    return report;
                }
            }
            report.addEntry(dfg1, dfg2, IncrementType.INC_TYPE_0, "r0");
        }
        return report;
    }

    private SimpleDirectedGraph<BtfVertex, DefaultEdge> getType1(String reg, String imm) {
        SimpleDirectedGraph<BtfVertex, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
        var ra = new BtfVertex(reg, BtfVertexType.REGISTER);
        var rb = new BtfVertex(reg, BtfVertexType.REGISTER);
        var rc = new BtfVertex(reg, BtfVertexType.REGISTER);
        var imm1 = new BtfVertex(imm, BtfVertexType.IMMEDIATE);
        var imm2 = new BtfVertex(imm, BtfVertexType.IMMEDIATE);
        var add1 = new BtfVertex("+", BtfVertexType.OPERATION);
        var add2 = new BtfVertex("+", BtfVertexType.OPERATION);

        graph.addVertex(ra);
        graph.addVertex(rb);
        graph.addVertex(rc);
        graph.addVertex(imm1);
        graph.addVertex(imm2);
        graph.addVertex(add1);
        graph.addVertex(add2);

        graph.addEdge(ra, add1);
        graph.addEdge(imm1, add1);
        graph.addEdge(add1, rb);
        graph.addEdge(rb, add2);
        graph.addEdge(imm2, add2);
        graph.addEdge(add2, rc);
        return graph;
    }
}
