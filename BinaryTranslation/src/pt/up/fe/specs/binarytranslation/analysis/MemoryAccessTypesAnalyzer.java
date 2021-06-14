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

package pt.up.fe.specs.binarytranslation.analysis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex.DataFlowVertexType;
import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.MemoryAddressDetector;
import pt.up.fe.specs.binarytranslation.analysis.graphs.templates.GraphTemplateFactory;
import pt.up.fe.specs.binarytranslation.analysis.graphs.templates.GraphTemplateReport;
import pt.up.fe.specs.binarytranslation.analysis.graphs.templates.GraphTemplateType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformRemoveTemporaryVertices;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class MemoryAccessTypesAnalyzer extends ATraceAnalyzer {

    public MemoryAccessTypesAnalyzer(ATraceInstructionStream stream, ELFProvider elf) {
        super(stream, elf);
    }

    public GraphTemplateReport analyzeSegment(int window) {
        var det = buildDetector(window);
        List<BinarySegment> segs = AnalysisUtils.getSegments(stream, det);
        List<Instruction> insts = det.getProcessedInsts();
        
        var title = elf.getFilename();
        var report = new GraphTemplateReport(title);
        
        for (var bb : segs) {
            var mad = new MemoryAddressDetector(bb, insts);
            var graphs = mad.detectGraphs();
            var storeCnt = 0;
            var loadCnt = 0;
            
            for (var graph : graphs) {
                var t1 = new TransformHexToDecimal(graph);
                t1.applyToGraph();
                var t2 = new TransformShiftsToMult(graph);
                t2.applyToGraph();
                var t3 = new TransformRemoveTemporaryVertices(graph);
                t3.applyToGraph();
                
                String id = "";
                if (GraphUtils.findAllNodesOfType(graph, DataFlowVertexType.LOAD_TARGET).size() != 0) {
                    loadCnt++;
                    id = "L" + loadCnt;
                } else {
                    storeCnt++;
                    id = "S" + storeCnt;
                }

                var graphCategory = matchGraph(graph);
                report.addEntry(graph, id, graphCategory, bb.getOccurences());
            }
            GraphTemplateReport.incrementLastID();
        }
        return report;
    }

    private GraphTemplateType matchGraph(Graph<DataFlowVertex, DefaultEdge> graph) {
        var exprGraph = GraphUtils.getExpressionGraph(graph);

        for (var type : GraphTemplateType.values()) {
            var template = GraphTemplateFactory.getTemplate(type).getGraph();
            var iso = new VF2GraphIsomorphismInspector<DataFlowVertex, DefaultEdge>(exprGraph, template,
                    new VertexComparator(), new EdgeComparator());
            if (iso.isomorphismExists())
                return type;
        }
        return GraphTemplateType.TYPE_0;
    }

    private class VertexComparator implements Comparator<DataFlowVertex> {
        @Override
        public int compare(DataFlowVertex o1, DataFlowVertex o2) {
            var type1 = o1.getType();
            var type2 = o2.getType();
            var label1 = o1.getLabel();
            var label2 = o2.getLabel();

            if (type1 == DataFlowVertexType.REGISTER && type2 == DataFlowVertexType.REGISTER)
                return 0;
            if (type1 == DataFlowVertexType.OPERATION && type2 == DataFlowVertexType.OPERATION) {
                if (label1.equals(label2))
                    return 0;
                else if ((label1.equals("+") || label1.equals("-")) && (label2.equals("+") || label2.equals("-")))
                    return 0;
                else
                    return -1;
            }
            if (type1 == DataFlowVertexType.IMMEDIATE && type2 == DataFlowVertexType.IMMEDIATE) {
                return 0;
            }
            if (type1 == DataFlowVertexType.MEMORY && type2 == DataFlowVertexType.MEMORY)
                return 0;
            return -1;
        }
    }

    private class EdgeComparator implements Comparator<DefaultEdge> {
        @Override
        public int compare(DefaultEdge e1, DefaultEdge e2) {
            return 0;
        }
    }
}
