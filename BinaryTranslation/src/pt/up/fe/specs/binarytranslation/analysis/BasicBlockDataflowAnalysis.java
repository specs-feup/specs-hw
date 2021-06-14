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
import java.util.List;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.DataFlowCriticalPath;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class BasicBlockDataflowAnalysis extends ATraceAnalyzer {

    public BasicBlockDataflowAnalysis(ATraceInstructionStream stream, ELFProvider elf) {
        super(stream, elf);
    }

    public List<DataFlowStatistics> analyze(int window) {
        var res = new ArrayList<DataFlowStatistics>();
        var det = buildDetector(window);
        List<BinarySegment> segs = AnalysisUtils.getSegments(stream, det);
        List<Instruction> insts = det.getProcessedInsts();

        for (var bb : segs) {

            var tracker = new BasicBlockOccurrenceTracker(bb, insts);
            var dfg = new BasicBlockDataFlowGraph(tracker);

            var pathfinder = new DataFlowCriticalPath(dfg);
            var path = pathfinder.calculatePath();

            var sources = new ArrayList<String>();
            var sinks = new ArrayList<String>();
            for (var v : pathfinder.findSinks()) {
                if (v.getType() == BtfVertexType.REGISTER)
                    sinks.add(v.getLabel());
            }
            for (var v : pathfinder.findSources()) {
                if (v.getType() == BtfVertexType.REGISTER)
                    sources.add(v.getLabel());
            }
            var stats = new DataFlowStatistics(dfg, path, bb.getInstructions(), sources, sinks);
            res.add(stats);
        }
        return res;
    }
}
