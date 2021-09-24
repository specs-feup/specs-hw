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
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.scheduling.ListScheduler;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformAddMemoryDependencies;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformRemoveZeroLatencyOps;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class BasicBlockSchedulingAnalyzer extends ABasicBlockAnalyzer {

    private boolean useDependencies;

    public BasicBlockSchedulingAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window,
            boolean useDependencies) {
        super(stream, elf, window);
        this.useDependencies = useDependencies;
    }

    public BasicBlockSchedulingAnalyzer(List<List<Instruction>> basicBlocks, boolean useDependencies) {
        super(basicBlocks);
        this.useDependencies = useDependencies;
    }

    public List<DataFlowStatistics> analyze(int[] repetitions, int[] alus, int[] memPorts) {
        var res = new ArrayList<DataFlowStatistics>();
        var segments = getBasicBlocks();

        for (var bb : segments) {
            for (var repetition : repetitions) {
                var dfg = new BasicBlockDataFlowGraph(bb, repetition);
                var t = new TransformRemoveZeroLatencyOps(dfg);
                dfg = (BasicBlockDataFlowGraph) t.applyToGraph();

                // Apply dependencies
                if (useDependencies) {
                    System.out.println("Adding extra dependency edges...");
                    var trans = new TransformAddMemoryDependencies(dfg);
                    dfg = (BasicBlockDataFlowGraph) trans.applyToGraph();
                }
                // Build temporary IDs
                int id = 0;
                for (var v : dfg.vertexSet()) {
                    v.setTempId(id);
                    id++;
                }

                System.out.println("Transformed graph:");
                System.out.println(GraphUtils.generateGraphURL(dfg));

                var stats = new DataFlowStatistics(dfg);
                stats.setInsts(bb).setRepetitions(repetition);

                System.out
                        .println("Scheduling a BB of " + this.elf.getFilename() + " with " + repetition + " repetitions");
                for (var aluN : alus) {
                    for (var memPortsN : memPorts) {
                        var scheduler = new ListScheduler(dfg);
                        var schedule = scheduler.scheduleWithDiscreteResources(aluN, memPortsN);
                        var total = schedule.getScheduleLatency();

                        System.out.println(
                                "Schedule length (ALU=" + aluN + ", MEM=" + memPortsN + "): " + total + " cycles");
                        stats.addSchedule(schedule);
                        stats.setRepetitions(repetition);
                    }
                }
                res.add(stats);
            }

        }
        return res;
    }
}