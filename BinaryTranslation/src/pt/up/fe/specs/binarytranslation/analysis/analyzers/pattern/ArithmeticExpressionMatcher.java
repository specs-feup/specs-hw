/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern;

import java.util.HashMap;
import java.util.List;

import org.jgrapht.GraphMapping;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegmentType;
import pt.up.fe.specs.binarytranslation.elf.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class ArithmeticExpressionMatcher extends APatternAnalyzer {
    public enum ArithmeticTemplates {
        ADD_0_CONSTANTS,    // (r1 + r2) + r3
        ADD_1_CONSTANT_A,   // (c1 + r2) + r3
        ADD_1_CONSTANT_B,   // (r1 + r2) + c1
        ADD_2_CONSTANTS,    // (c1 + r2) + c2
        
        MAD_0_CONSTANTS,    // r1 * r2 + r3
        MAD_1_CONSTANT_A,   // c1 * r2 + r3
        MAD_1_CONSTANT_B,   // r1 * r2 + c1
        MAD_2_CONSTANTS     // c1 * r2 + c2
        
        // Should never happen, as they're solved statically:
        // (c1 + c2) + r3
        // (c1 + c2) + c3
        // (c1 * c2) + r3
        // (c1 * c2) + c3       
    }
    
    private int repetitions = 1;

    public ArithmeticExpressionMatcher(ATraceInstructionStream stream, ZippedELFProvider elf, int window, BinarySegmentType type) {
        super(stream, elf, window, type);
    }
    
    public ArithmeticExpressionMatcher(ATraceInstructionStream stream, ZippedELFProvider elf, int window, int repetitions, BinarySegmentType type) {
        super(stream, elf, window, type);
        this.repetitions = repetitions;
    }

    @Override
    protected APatternReport matchTemplates(List<BinarySegment> segs) {
        var report = new ArithmeticPatternReport();
        
        for (var bb : segs) {
            var insts = bb.getInstructions();
            var dfg = new BasicBlockDataFlowGraph(insts, repetitions);
            var matches = matchToSequences(dfg);
            
            report.addEntry(matches);
        }
        
        return report;
    }
    
    private HashMap<ArithmeticTemplates, List<GraphMapping<BtfVertex, DefaultEdge>>> matchToSequences(BasicBlockDataFlowGraph dfg) {
        SimpleDirectedGraph<BtfVertex, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
        var r1 = new BtfVertex("r1", BtfVertexType.REGISTER);
        var r2 = new BtfVertex("r2", BtfVertexType.REGISTER);
        var r3 = new BtfVertex("r3", BtfVertexType.REGISTER);
        var temp = new BtfVertex("temp", BtfVertexType.REGISTER);
        var op1 = new BtfVertex("+", BtfVertexType.OPERATION);
        var op2 = new BtfVertex("+", BtfVertexType.OPERATION);
        graph.addVertex(r1);
        graph.addVertex(r2);
        graph.addVertex(r3);
        graph.addVertex(temp);
        graph.addVertex(op1);
        graph.addVertex(op2);
        graph.addEdge(r1, op1);
        graph.addEdge(r2, op1);
        graph.addEdge(op1, temp);
        graph.addEdge(temp, op2);
        graph.addEdge(r3, op2);
        
        var res = new HashMap<ArithmeticTemplates, List<GraphMapping<BtfVertex, DefaultEdge>>>();
        
        var match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.ADD_0_CONSTANTS, match.getMatchedGraphs());

        r1.setType(BtfVertexType.IMMEDIATE);
        r2.setType(BtfVertexType.REGISTER);
        r3.setType(BtfVertexType.REGISTER);
        match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.ADD_1_CONSTANT_A, match.getMatchedGraphs());
        
        r1.setType(BtfVertexType.REGISTER);
        r2.setType(BtfVertexType.REGISTER);
        r3.setType(BtfVertexType.IMMEDIATE);
        match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.ADD_1_CONSTANT_B, match.getMatchedGraphs());
        
        r1.setType(BtfVertexType.IMMEDIATE);
        r2.setType(BtfVertexType.REGISTER);
        r3.setType(BtfVertexType.IMMEDIATE);
        match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.ADD_2_CONSTANTS, match.getMatchedGraphs());

        op1.setLabel("*");
        
        r1.setType(BtfVertexType.REGISTER);
        r2.setType(BtfVertexType.REGISTER);
        r3.setType(BtfVertexType.REGISTER);
        match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.MAD_0_CONSTANTS, match.getMatchedGraphs());
        
        r1.setType(BtfVertexType.IMMEDIATE);
        r2.setType(BtfVertexType.REGISTER);
        r3.setType(BtfVertexType.REGISTER);
        match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.MAD_1_CONSTANT_A, match.getMatchedGraphs());
        
        r1.setType(BtfVertexType.REGISTER);
        r2.setType(BtfVertexType.REGISTER);
        r3.setType(BtfVertexType.IMMEDIATE);
        match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.MAD_1_CONSTANT_B, match.getMatchedGraphs());
        
        r1.setType(BtfVertexType.IMMEDIATE);
        r2.setType(BtfVertexType.REGISTER);
        r3.setType(BtfVertexType.IMMEDIATE);
        match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.MAD_2_CONSTANTS, match.getMatchedGraphs());
        
        return res;
    }
}
