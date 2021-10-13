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

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.memory.MemoryAddressDetector;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.APatternAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.GraphTemplateReport;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.templates.GraphTemplateType;
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
    protected void matchTemplates(GraphTemplateReport report, BinarySegment bb) {
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

            for (var type : GraphTemplateType.values()) {
                var graphCategory = matchGraphToTemplate(graph, type);
                report.addEntry(graph, id, graphCategory, bb.getOccurences());
            }
        }
        GraphTemplateReport.incrementLastID();
    }

}
