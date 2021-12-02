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

package pt.up.fe.specs.binarytranslation.analysis.analyzers.reporters;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.GraphMapping;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.ASegmentAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.MemoryAccessTypesAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.analysis.graphs.templates.GraphTemplateType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformRemoveTemporaryVertices;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegmentType;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class SegmentAddressGenerationUnitAnalyzer extends ASegmentAnalyzer {

    public SegmentAddressGenerationUnitAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window,
            BinarySegmentType type) {
        super(stream, elf, window, type);
    }
    
    public List<DataFlowStatistics> analyze() {
        return analyze(1);
    }
    
    public List<DataFlowStatistics> analyze(int repetitions) {
        var res = new ArrayList<DataFlowStatistics>();
        var segments = getSegmentsAsList();

        for (var bb : segments) {
            var dfg = new BasicBlockDataFlowGraph(bb, repetitions);
 
            modifyWithAGUs(dfg);

            var stats = new DataFlowStatistics(dfg, bb);
            stats.setRepetitions(repetitions);
            res.add(stats);
        }
        return res;
    }

    private static void preprocessGraph(BasicBlockDataFlowGraph graph) {
        var t1 = new TransformHexToDecimal(graph);
        t1.applyToGraph();
        var t2 = new TransformShiftsToMult(graph);
        t2.applyToGraph();
        var t3 = new TransformRemoveTemporaryVertices(graph);
        t3.applyToGraph();
    }

    public static void modifyWithAGUs(BasicBlockDataFlowGraph graph) {
        preprocessGraph(graph);
        
        var aguID = 1;
        // TODO: plug in AGU simulation class
        // let's use dot for vizualzation for now

        for (var type : GraphTemplateType.values()) {
            if (type == GraphTemplateType.TYPE_0)
                continue;
            SimpleDirectedGraph<BtfVertex, DefaultEdge> template = type.getTemplate().getGraph();
            modifyTemplate(template);

            var match = MemoryAccessTypesAnalyzer.matchGraphToTemplate(graph, template);
            if (match.isMatch()) {
                for (var g : match.getMatchedGraphs()) {
                    var success = bindAguToMatch(g, aguID, type, graph);
                    if (success)
                        aguID++;
                }
            }
        }
    }

    private static void modifyTemplate(SimpleDirectedGraph<BtfVertex, DefaultEdge> template) {
        for (var v : template.vertexSet()) {
            if (template.outDegreeOf(v) == 0) {
                v.setType(BtfVertexType.MEMORY);
            }
        }
    }

    private static boolean bindAguToMatch(GraphMapping<BtfVertex, DefaultEdge> mapping, int aguID, GraphTemplateType type,
            BasicBlockDataFlowGraph graph) {
        System.out.println("AGU BOUND - " + type.name());

        var aguVertices = getAguVertices(mapping, graph, true);
        var nonAguVertices = getAguVertices(mapping, graph, false);
        var memVertex = BtfVertex.nullVertex;
        for (var v : aguVertices) {
            if (v.getType() == BtfVertexType.MEMORY) {
                memVertex = v;
            }
            if (!graph.vertexSet().contains(v)) {
                System.out.println("Cant create ALU, as one or more nodes have already been removed");
                return false;
            }
        }
        if (memVertex == BtfVertex.nullVertex) {
            System.out.println("Cant create ALU, as the vertex cannot be found");
            return false;
        }
        
        // Create AGU vertex
        var latency = calculateLatency(aguVertices);
        var agu = new BtfVertex("AGU " + aguID + " (" + type.toString() + ")\n" + memVertex.getLabel() + "\nLatency = " + latency, BtfVertexType.AGU);
        agu.setLatency(latency);
        graph.addVertex(agu);
        
        // If load, connect AGU to RD
        if (memVertex.getLabel().equals("Load")) {
            for (var v : Graphs.successorListOf(graph, memVertex))
                graph.addEdge(agu, v);
        }
        // If store, connect RD to AGU
        else {
            for (var v : Graphs.predecessorListOf(graph, memVertex)) {
                if (!aguVertices.contains(v))
                    graph.addEdge(v, agu);
            }
        }

        // connect sources and sinks to AGU
        for (var v : nonAguVertices) {
            for (var u : Graphs.successorListOf(graph, v)) {
                if (aguVertices.contains(u)) {
                    graph.removeVertex(u);
                    graph.addEdge(v, agu);
                }
            }
            // Should never happen, I think...
            for (var u : Graphs.predecessorListOf(graph, v)) {
                if (aguVertices.contains(u)) {
                    graph.removeVertex(u);
                    graph.addEdge(agu, v);
                }
            }
        }

        // remove remaining vertices
        graph.removeAllVertices(aguVertices);
        return true;
    }

    private static int calculateLatency(List<BtfVertex> aguVertices) {
        var lat = 0;
        for (var v : aguVertices)
            lat += v.getLatency();
        return lat;
    }

    private static List<BtfVertex> getAguVertices(GraphMapping<BtfVertex, DefaultEdge> mapping,
            BasicBlockDataFlowGraph graph, boolean aguVertices) {
        var res = new ArrayList<BtfVertex>();
        BtfVertex mem = BtfVertex.nullVertex;
        for (var v : graph.vertexSet()) {
            if (mapping.getVertexCorrespondence(v, true) != null) {
                if (aguVertices) {
                    if (v.getType() == BtfVertexType.IMMEDIATE || v.getType() == BtfVertexType.OPERATION
                            || v.getType() == BtfVertexType.MEMORY)
                        res.add(v);
                } else {
                    if (v.getType() == BtfVertexType.REGISTER)
                        res.add(v);
                }
                for (var u : Graphs.successorListOf(graph, v)) {
                    if (u.getType() == BtfVertexType.MEMORY) {
                        mem = u;
                    }
                }
            }
        }
        return res;
    }
}
