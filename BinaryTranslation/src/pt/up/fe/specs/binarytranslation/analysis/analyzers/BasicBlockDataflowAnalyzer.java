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

import java.util.ArrayList;
import java.util.List;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowCriticalPath;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.ocurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class BasicBlockDataflowAnalyzer extends ABasicBlockAnalyzer {
    public BasicBlockDataflowAnalyzer(ATraceInstructionStream stream, ELFProvider elf) {
        super(stream, elf);
    }

    public List<DataFlowStatistics> analyzeWithDetector(int window, int repetitions) {

        var det = buildDetector(window);
        List<BinarySegment> segs = AnalysisUtils.getSegments(stream, det);
        List<List<Instruction>> segments = new ArrayList<>();
        if (segs == null) {
            System.out.println("No basic blocks found! Aborting...");
            return new ArrayList<DataFlowStatistics>();
        }
        for (var bb : segs) {
            segments.add(bb.getInstructions());
        }
        return analyze(segments, repetitions);
    }

    public List<DataFlowStatistics> analyzeWithStaticBlock(List<Instruction> segment, int repetitions) {
        var arr = new ArrayList<List<Instruction>>();
        arr.add(segment);
        return analyze(arr, repetitions);
    }

    private List<DataFlowStatistics> analyze(List<List<Instruction>> segments, int repetitions) {
        var res = new ArrayList<DataFlowStatistics>();
        for (var bb : segments) {

            var dfg = new BasicBlockDataFlowGraph(bb, repetitions);

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
            var stats = new DataFlowStatistics(dfg, path, bb, repetitions, sources, sinks);
            res.add(stats);
        }
        return res;
    }
}
