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

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.MemoryAddressDetector;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformRemoveTemporaryVertices;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class MemoryAccessTypesAnalyzer extends ATraceAnalyzer {

    public MemoryAccessTypesAnalyzer(ATraceInstructionStream stream, ELFProvider elf) {
        super(stream, elf);
    }

    public ArrayList<String> analyze(int window) {
        // var trace = AnalysisUtils.getCompleteTrace(stream,
        // elf.getKernelStart() == null ? 0 : elf.getKernelStart().longValue(),
        // elf.getKernelStop() == null ? -1 : elf.getKernelStop().longValue());
        // System.out.println("Trace has " + trace.size() + " instructions");

        var det = buildDetector(window);
        List<BinarySegment> segs = AnalysisUtils.getSegments(stream, det);
        List<Instruction> insts = det.getProcessedInsts();

        var graphList = new ArrayList<String>();
        graphList.add(elf.getFilename());

        for (var bb : segs) {
            var mad = new MemoryAddressDetector(bb, insts);
            var graphs = mad.detectGraphs();
            for (var graph : graphs) {
                var t1 = new TransformHexToDecimal(graph);
                t1.applyToGraph();
                var t2 = new TransformShiftsToMult(graph);
                t2.applyToGraph();
                var t3 = new TransformRemoveTemporaryVertices(graph);
                t3.applyToGraph();

                var graphCategory = matchGraph(graph);
                System.out.println(graphCategory);
            }

            String dot = GraphUtils.graphToDot(GraphUtils.mergeGraphs(graphs), elf.toString());
            String url = GraphUtils.generateGraphURL(dot);
            graphList.add(url);
            // MemoryAddressAnalyzer.printExpressions(graphs);
        }
        return graphList;
    }

    // WIP - example of isomorphism
    private boolean matchGraph(Graph<AddressVertex, DefaultEdge> graph) {
        var template = new DefaultDirectedGraph<>(DefaultEdge.class);
        template.addVertex(new AddressVertex("r1", AddressVertexType.REGISTER));

        var iso = new VF2GraphIsomorphismInspector(graph, template, new VertexComparator(), new EdgeComparator());
        return iso.isomorphismExists();
    }

    private class VertexComparator implements Comparator<AddressVertex> {
        @Override
        public int compare(AddressVertex o1, AddressVertex o2) {
            var type1 = o1.getType();
            var type2 = o2.getType();
            var label1 = o1.getLabel();
            var label2 = o2.getLabel();

            if (type1 == AddressVertexType.REGISTER && type2 == AddressVertexType.REGISTER)
                return 0;
            if (type1 == AddressVertexType.OPERATION && type2 == AddressVertexType.OPERATION) {
                if (label1.equals(label2))
                    return 0;
                else if ((label1.equals("+") || label1.equals("-")) && (label2.equals("+") || label2.equals("-")))
                    return 0;
                else
                    return -1;
            }
            if (type1 == AddressVertexType.IMMEDIATE && type2 == AddressVertexType.IMMEDIATE) {
                if (label1.equals(label2))
                    return 0;
                else
                    return -1;
            }
            if (type1 == AddressVertexType.MEMORY && type2 == AddressVertexType.MEMORY)
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
