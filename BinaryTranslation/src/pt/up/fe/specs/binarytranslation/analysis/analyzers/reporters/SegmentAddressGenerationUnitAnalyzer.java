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
import pt.up.fe.specs.binarytranslation.elf.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class SegmentAddressGenerationUnitAnalyzer extends ASegmentAnalyzer {

    public SegmentAddressGenerationUnitAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window,
            BinarySegmentType type) {
        super(stream, elf, window, type);
    }

    public List<DataFlowStatistics> analyze() {
        return analyze(1, false);
    }

    public List<DataFlowStatistics> analyze(int repetitions, boolean useMemory) {
        var res = new ArrayList<DataFlowStatistics>();
        var segments = getSegmentsAsList();

        for (var bb : segments) {
            var dfg = new BasicBlockDataFlowGraph(bb, repetitions);

            modifyWithAGUs(dfg, useMemory);

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

    public static void modifyWithAGUs(BasicBlockDataFlowGraph graph, boolean useMemory) {
        preprocessGraph(graph);

        var aguID = 1;
        // TODO: plug in AGU simulation class
        // let's use dot for vizualzation for now

        // Total number of mem ops in graph (AGUs cannot exceed it)
        var memCount = 0;
        for (var v : graph.vertexSet()) {
            if (v.getType() == BtfVertexType.MEMORY)
                memCount++;
        }
        var processedMemOps = new ArrayList<BtfVertex>();

        for (var type : GraphTemplateType.getAguTemplates()) {
            // Get template and match dfg to template
            SimpleDirectedGraph<BtfVertex, DefaultEdge> template = type.getTemplate().getGraph();
            modifyTemplate(template);
            var match = MemoryAccessTypesAnalyzer.matchGraphToTemplate(graph, template);

            // Go through each match
            if (match.isMatch()) {
                for (var g : match.getMatchedGraphs()) {

                    // Get the memory vertex of the match. If it has already been
                    // assigned to an AGU, skip it
                    var memVertex = getMatchMemoryVertex(graph, g);
                    if (processedMemOps.contains(memVertex))
                        continue;

                    // Try to bind an AGU to this memory access
                    var success = attemptToBindAGU(g, aguID, type, graph, memVertex);
                    if (success) {
                        processedMemOps.add(memVertex);
                        memVertex.setLatency(1);
                        aguID++;
                    }

                    // If we've already assigned every memory access to an AGU, end
                    if (processedMemOps.size() >= memCount)
                        return;
                }
            }
        }
    }

    private static BtfVertex getMatchMemoryVertex(BasicBlockDataFlowGraph graph,
            GraphMapping<BtfVertex, DefaultEdge> g) {
        var memVertex = BtfVertex.nullVertex;

        for (var v : graph.vertexSet()) {
            if (g.getVertexCorrespondence(v, true) != null) {
                if (v.getType() == BtfVertexType.MEMORY)
                    memVertex = v;
            }
        }
        return memVertex;
    }

    private static void modifyTemplate(SimpleDirectedGraph<BtfVertex, DefaultEdge> template) {
        for (var v : template.vertexSet()) {
            if (template.outDegreeOf(v) == 0) {
                v.setType(BtfVertexType.MEMORY);
            }
        }
    }

    private static boolean attemptToBindAGU(GraphMapping<BtfVertex, DefaultEdge> mapping, int aguID,
            GraphTemplateType type, BasicBlockDataFlowGraph g, BtfVertex memV) {

        var aguVertices = getAguVertices(mapping, g, true);
        var nonAguVertices = getAguVertices(mapping, g, false);

        // Get RD register of memory access
        var rd = BtfVertex.nullVertex;
        if (memV.getLabel().equals("Load")) {
            for (var v : Graphs.successorListOf(g, memV))
                rd = v;
        } else {
            for (var v : Graphs.successorListOf(g, memV)) {
                if (!nonAguVertices.contains(v) && !aguVertices.contains(v))
                    rd = v;
            }
        }
        if (rd == BtfVertex.nullVertex) {
            System.out.println("Error in binding: couldn't determine RD");
            return false;
        }
        
        // Create AGU vertex
        var latency = calculateLatency(aguVertices) + 1;
        var agu = new BtfVertex("AGU " + aguID + " (" + type.toString() + ")", BtfVertexType.AGU);
        agu.setLatency(latency);
        g.addVertex(agu);
        
        // Add input nodes to AGU
        for (var v : nonAguVertices) {
            g.addEdge(v, agu);
        }
        
        // Connect AGU to mem vertex
        g.addEdge(agu, memV);
        
        // Remove all agu vertices
        g.removeAllVertices(aguVertices);
        
        return true;
    }

    private static boolean bindAguToMatch(GraphMapping<BtfVertex, DefaultEdge> mapping, int aguID,
            GraphTemplateType type, BasicBlockDataFlowGraph graph, boolean useMemory) {
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

        if (!useMemory) {
            memVertex.setLatency(1);
        }
        int latency = calculateLatency(aguVertices);
        var latStr = calculateLatencyString(aguVertices);

        // Create AGU vertex
        var agu = new BtfVertex("AGU " + aguID + " (" + type.toString() + ")\n" + latStr,
                BtfVertexType.AGU);
        agu.setLatency(latency);
        graph.addVertex(agu);

        var rd = BtfVertex.nullVertex;

        // If load, connect AGU to RD
        if (memVertex.getLabel().equals("Load")) {
            for (var v : Graphs.successorListOf(graph, memVertex)) {
                rd = v;
                graph.addEdge(agu, v);
            }
        }
        // If store, connect RD to AGU
        else {
            for (var v : Graphs.predecessorListOf(graph, memVertex)) {
                if (!aguVertices.contains(v)) {
                    rd = v;
                    graph.addEdge(v, agu);
                }
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

        if (!useMemory) {
            if (graph.containsVertex(memVertex))
                graph.removeVertex(memVertex);
            graph.addVertex(memVertex);

            // Case Load: disconnect RD from
            if (memVertex.getLabel().equals("Load")) {
                for (var rdVertex : Graphs.successorListOf(graph, agu)) {
                    graph.removeAllEdges(agu, rdVertex);
                    graph.addEdge(agu, memVertex);
                    graph.addEdge(memVertex, rdVertex);
                }
            } else {
                for (var rdVertex : Graphs.predecessorListOf(graph, agu)) {
                    if (!nonAguVertices.contains(rdVertex)) {
                        graph.removeAllEdges(rdVertex, agu);
                        graph.addEdge(rdVertex, memVertex);
                    }
                }
            }
        }

        return false;
    }

    private static int calculateLatency(List<BtfVertex> aguVertices) {
        var lat = 0;
        for (var v : aguVertices)
            lat += v.getLatency();
        return lat;
    }

    private static String calculateLatencyString(List<BtfVertex> aguVertices) {
        var lat = new ArrayList<String>();
        for (var v : aguVertices)
            lat.add("" + v.getLatency());
        return "[" + String.join(",", lat) + "]";
    }

    private static List<BtfVertex> getAguVertices(GraphMapping<BtfVertex, DefaultEdge> mapping,
            BasicBlockDataFlowGraph graph, boolean aguVertices) {
        var res = new ArrayList<BtfVertex>();
        BtfVertex mem = BtfVertex.nullVertex;
        for (var v : graph.vertexSet()) {
            if (mapping.getVertexCorrespondence(v, true) != null) {
                if (aguVertices) {
                    if (v.getType() == BtfVertexType.IMMEDIATE || v.getType() == BtfVertexType.OPERATION)
                        res.add(v);
                } else {
                    if (v.getType() == BtfVertexType.REGISTER)
                        res.add(v);
                }
                // for (var u : Graphs.successorListOf(graph, v)) {
                // if (u.getType() == BtfVertexType.MEMORY) {
                // mem = u;
                // }
                // }
            }
        }
        return res;
    }
}
