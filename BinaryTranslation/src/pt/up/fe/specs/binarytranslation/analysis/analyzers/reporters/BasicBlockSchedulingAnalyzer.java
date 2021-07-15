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

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.analysis.analyzers.ABasicBlockAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.scheduling.ListScheduler;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformRemoveZeroLatencyOps;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class BasicBlockSchedulingAnalyzer extends ABasicBlockAnalyzer {

    public BasicBlockSchedulingAnalyzer(ATraceInstructionStream stream, ELFProvider elf, int window) {
        super(stream, elf, window);
    }

    public BasicBlockSchedulingAnalyzer(List<List<Instruction>> basicBlocks) {
        super(basicBlocks);
    }

    public List<DataFlowStatistics> analyze(int repetitions, int[] alus, int[] memPorts) {
        var res = new ArrayList<DataFlowStatistics>();
        var segments = getBasicBlocks();

        for (var bb : segments) {
            var dfg = new BasicBlockDataFlowGraph(bb, repetitions);
            var t = new TransformRemoveZeroLatencyOps(dfg);
            dfg = (BasicBlockDataFlowGraph) t.applyToGraph();
            var stats = new DataFlowStatistics(dfg);
            stats.setInsts(bb).setRepetitions(repetitions);
            
            for (var aluN : alus) {
                for (var memPortsN : memPorts) {
                    var sched = new ListScheduler(dfg);
                    var s = sched.schedule(aluN, memPortsN);
                    var total = sched.getScheduleLength(s);
                    System.out.println("Schedule length (ALU=" + aluN + ", MEM=" + memPortsN + "): " + total + " cycles");
                    stats.addSchedule(total);
                    stats.setRepetitions(repetitions);
                }
            }
            res.add(stats);

        }
        return res;
    }
}