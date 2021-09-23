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

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.ABasicBlockAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowCriticalPath;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.ocurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.scheduling.ListScheduler;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformRemoveZeroLatencyOps;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class BasicBlockDataFlowAnalyzer extends ABasicBlockAnalyzer {

    public BasicBlockDataFlowAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        super(stream, elf, window);
    }
    
    public BasicBlockDataFlowAnalyzer(List<List<Instruction>> basicBlocks) {
        super(basicBlocks);
    }

    public List<DataFlowStatistics> analyze(int repetitions) {
        var res = new ArrayList<DataFlowStatistics>();
        var segments = getBasicBlocks();
        
        for (var bb : segments) {

            var dfg = new BasicBlockDataFlowGraph(bb, repetitions);
            
            // Critical path
            var pathfinder = new DataFlowCriticalPath(dfg);
            var path = pathfinder.calculatePath();

            // Sources and sinks
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
            
            var stats = new DataFlowStatistics(dfg);
            stats.setPath(path).setInsts(bb).setRepetitions(repetitions).setSources(sources).setSinks(sinks);

            res.add(stats);
        }
        return res;
    }
}
