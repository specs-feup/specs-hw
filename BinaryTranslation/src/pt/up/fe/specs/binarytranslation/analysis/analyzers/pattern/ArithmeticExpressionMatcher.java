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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.GraphMapping;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class ArithmeticExpressionMatcher extends APatternAnalyzer {

    public ArithmeticExpressionMatcher(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        super(stream, elf, window);
    }

    @Override
    protected APatternReport matchTemplates(List<BinarySegment> segs) {
        var report = new ArithmeticPatternReport();
        
        for (var bb : segs) {
            var insts = bb.getInstructions();
            var dfg = new BasicBlockDataFlowGraph(insts, 1);
            var matches = matchToAddSequence(dfg);
            
            var types = new ArrayList<String>();
            for (var key : matches.keySet()) {
                for (int i = 0; i < matches.get(key).size(); i++) {
                    types.add(key.toString());
                }
            }
            report.addEntry(String.join(",", types));
        }
        
        return report;
    }
    
    private HashMap<ArithmeticTemplates, List<GraphMapping<BtfVertex, DefaultEdge>>> matchToAddSequence(BasicBlockDataFlowGraph dfg) {
        SimpleDirectedGraph<BtfVertex, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
        var r1 = new BtfVertex("r1", BtfVertexType.REGISTER);
        var r2 = new BtfVertex("r2", BtfVertexType.REGISTER);
        var r3 = new BtfVertex("r3", BtfVertexType.REGISTER);
        var add1 = new BtfVertex("+", BtfVertexType.OPERATION);
        var add2 = new BtfVertex("+", BtfVertexType.OPERATION);
        graph.addVertex(r1);
        graph.addVertex(r2);
        graph.addVertex(r3);
        graph.addVertex(add1);
        graph.addVertex(add2);
        graph.addEdge(r1, add1);
        graph.addEdge(r2, add1);
        graph.addEdge(add1, add2);
        graph.addEdge(r3, add2);
        
        
        var res = new HashMap<ArithmeticTemplates, List<GraphMapping<BtfVertex, DefaultEdge>>>();
        
        var match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.TRIPLE_ADD_C0, match.getMatchedGraphs());

        r1.setType(BtfVertexType.IMMEDIATE);
        r2.setType(BtfVertexType.REGISTER);
        r3.setType(BtfVertexType.REGISTER);
        match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.TRIPLE_ADD_C1A, match.getMatchedGraphs());
        
        r1.setType(BtfVertexType.REGISTER);
        r2.setType(BtfVertexType.REGISTER);
        r3.setType(BtfVertexType.IMMEDIATE);
        match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.TRIPLE_ADD_C1B, match.getMatchedGraphs());
        
        r1.setType(BtfVertexType.IMMEDIATE);
        r2.setType(BtfVertexType.IMMEDIATE);
        r3.setType(BtfVertexType.REGISTER);
        match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.TRIPLE_ADD_C2A, match.getMatchedGraphs());
        
        r1.setType(BtfVertexType.IMMEDIATE);
        r2.setType(BtfVertexType.REGISTER);
        r3.setType(BtfVertexType.IMMEDIATE);
        match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.TRIPLE_ADD_C2B, match.getMatchedGraphs());
        
        r1.setType(BtfVertexType.IMMEDIATE);
        r2.setType(BtfVertexType.IMMEDIATE);
        r3.setType(BtfVertexType.IMMEDIATE);
        match = this.matchGraphToTemplate(dfg, graph);
        res.put(ArithmeticTemplates.TRIPLE_ADD_C3, match.getMatchedGraphs());
        
        return res;
    }
    
    private int matchToMultiplyAddSequence() {
        return 0;
    }
    
    public enum ArithmeticTemplates {
        TRIPLE_ADD_C0,      // (r1 + r2) + r3
        TRIPLE_ADD_C1A,     // (c1 + r2) + r3
        TRIPLE_ADD_C1B,     // (r1 + r2) + c1
        TRIPLE_ADD_C2A,     // (c1 + c2) + r3
        TRIPLE_ADD_C2B,     // (c1 + rb) + c2
        TRIPLE_ADD_C3,      // (c1 + c2) + c3
        MAD_C0  
    }
}
