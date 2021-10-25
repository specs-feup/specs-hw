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

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegmentType;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class LoopIncrementPatternAnalyzer extends APatternAnalyzer {

    public LoopIncrementPatternAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window, BinarySegmentType type) {
        super(stream, elf, window, type);
    }
    
    @Override
    protected APatternReport matchTemplates(List<BinarySegment> segs) {
        var report = new LoopIncrementReport();

        for (var bb : segs) {
            var insts = bb.getInstructions();
            var dfg1 = new BasicBlockDataFlowGraph(insts, 1);
            var dfg2 = new BasicBlockDataFlowGraph(insts, 2);
            var foundTypes = new ArrayList<String>();
            var foundRegs = new ArrayList<String>();
            var foundImms = new ArrayList<String>();

            // Check strict template first
            var template = getTemplate(true);
            var match = matchGraphToTemplate(dfg2, template, true, true);
            if (match.isMatch()) {
                for (int i = 0; i < match.getStrictImms().size(); i++)
                    foundTypes.add(IncrementType.INC_TYPE_1.toString());
                foundRegs.addAll(match.getStrictRegisters());
                foundImms.addAll(match.getStrictImms());
            }

            // Check relaxed template after
            template = getTemplate(false);
            match = matchGraphToTemplate(dfg2, template, true, true);
            if (match.isMatch()) {
                for (int i = 0; i < match.getStrictRegisters().size(); i++) {
                    var reg = match.getStrictRegisters().get(i);
                    var imm = match.getStrictImms().get(i);

                    if (!foundRegs.contains(reg)) {
                        foundTypes.add(IncrementType.INC_TYPE_2.toString());
                        foundRegs.add(reg);
                        foundImms.add(imm);
                    }
                }
            }

            if (foundTypes.size() == 0)
                report.addEntry(dfg1, dfg2, IncrementType.INC_TYPE_0.toString(), "--", "--");
            else
                report.addEntry(dfg1, dfg2, String.join("|", foundTypes), String.join("|", foundRegs), String.join("|", foundImms));
        }
        return report;
    }

    private SimpleDirectedGraph<BtfVertex, DefaultEdge> getTemplate(boolean extraNode) {
        SimpleDirectedGraph<BtfVertex, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
        var ra = new BtfVertex("ra", BtfVertexType.REGISTER);
        var rb = new BtfVertex("ra", BtfVertexType.REGISTER);
        var rc = new BtfVertex("ra", BtfVertexType.REGISTER);
        var imm1 = new BtfVertex("1", BtfVertexType.IMMEDIATE);
        var imm2 = new BtfVertex("1", BtfVertexType.IMMEDIATE);
        var add1 = new BtfVertex("+", BtfVertexType.OPERATION);
        var add2 = new BtfVertex("+", BtfVertexType.OPERATION);

        graph.addVertex(ra);
        graph.addVertex(rb);
        if (extraNode)
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
        if (extraNode)
            graph.addEdge(add2, rc);
        return graph;
    }
}
