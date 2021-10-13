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
import pt.up.fe.specs.binarytranslation.analysis.analyzers.memory.MemoryAddressDetector;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.templates.GraphTemplateType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformRemoveTemporaryVertices;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class MemoryAccessTypesAnalyzer extends APatternAnalyzer {

    public MemoryAccessTypesAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        super(stream, elf, window);
    }

    @Override
    protected APatternReport matchTemplates(List<BinarySegment> segs) {

        var title = elf.getFilename();
        var report = new MemoryPatternReport(title);

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

                var finalType = GraphTemplateType.TYPE_0;
                for (var type : GraphTemplateType.values()) {
                    SimpleDirectedGraph<BtfVertex, DefaultEdge> template = type.getTemplate().getGraph();
                    var match = matchGraphToTemplate(graph, template);
                    if (match)
                        finalType = type;
                }
                report.addEntry(graph, id, finalType, bb.getOccurences());
            }
            MemoryPatternReport.incrementLastID();
        }
        return report;
    }

}
