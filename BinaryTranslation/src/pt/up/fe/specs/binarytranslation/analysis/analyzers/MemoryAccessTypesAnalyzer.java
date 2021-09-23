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

package pt.up.fe.specs.binarytranslation.analysis.analyzers;

import java.util.Comparator;
import org.jgrapht.Graph;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.memory.MemoryAddressDetector;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.GraphTemplateReport;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.templates.GraphTemplateType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformRemoveTemporaryVertices;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class MemoryAccessTypesAnalyzer extends ABasicBlockAnalyzer {

    public MemoryAccessTypesAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        super(stream, elf, window);
    }

    public GraphTemplateReport analyzeSegment() {
        var segs = getBasicBlockSegments();
        
        var title = elf.getFilename();
        var report = new GraphTemplateReport(title);
        
        for (var bb : segs) {
            var mad = new MemoryAddressDetector(bb);
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
                if (GraphUtils.findAllNodesOfType(graph, BtfVertexType.LOAD_TARGET).size() != 0) {
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

    private GraphTemplateType matchGraph(Graph<BtfVertex, DefaultEdge> graph) {
        var exprGraph = GraphUtils.getExpressionGraph(graph);

        for (var type : GraphTemplateType.values()) {
            var template = type.getTemplateGraph();
            var iso = new VF2GraphIsomorphismInspector<BtfVertex, DefaultEdge>(exprGraph, template,
                    new VertexComparator(), new EdgeComparator());
            if (iso.isomorphismExists())
                return type;
        }
        return GraphTemplateType.TYPE_0;
    }

    private class VertexComparator implements Comparator<BtfVertex> {
        @Override
        public int compare(BtfVertex o1, BtfVertex o2) {
            var type1 = o1.getType();
            var type2 = o2.getType();
            var label1 = o1.getLabel();
            var label2 = o2.getLabel();

            if (type1 == BtfVertexType.REGISTER && type2 == BtfVertexType.REGISTER)
                return 0;
            if (type1 == BtfVertexType.OPERATION && type2 == BtfVertexType.OPERATION) {
                if (label1.equals(label2))
                    return 0;
                else if ((label1.equals("+") || label1.equals("-")) && (label2.equals("+") || label2.equals("-")))
                    return 0;
                else
                    return -1;
            }
            if (type1 == BtfVertexType.IMMEDIATE && type2 == BtfVertexType.IMMEDIATE) {
                return 0;
            }
            if (type1 == BtfVertexType.MEMORY && type2 == BtfVertexType.MEMORY)
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
