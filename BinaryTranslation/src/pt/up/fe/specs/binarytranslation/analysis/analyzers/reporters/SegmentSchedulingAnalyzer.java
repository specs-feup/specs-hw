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
import pt.up.fe.specs.binarytranslation.analysis.analyzers.ASegmentAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.scheduling.ListScheduler;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dependency.DependencyGraph;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformAddMemoryDependencies;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformRemoveZeroLatencyOps;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegmentType;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class SegmentSchedulingAnalyzer extends ASegmentAnalyzer {

    private boolean useDependencies = false;
    private boolean useAGUs = false;

    public SegmentSchedulingAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window,
            boolean useDependencies) {
        super(stream, elf, window);
        this.useDependencies = useDependencies;
    }

    public SegmentSchedulingAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window,
            boolean useDependencies, BinarySegmentType type, boolean useAGUs) {
        super(stream, elf, window, type);
        this.useDependencies = useDependencies;
        this.useAGUs  = useAGUs;
    }

    public SegmentSchedulingAnalyzer(List<List<Instruction>> basicBlocks, boolean useDependencies) {
        super(basicBlocks);
        this.useDependencies = useDependencies;
    }

    public List<DataFlowStatistics> analyze(int[] repetitions, int[] alus, int[] memPorts) {
        var res = new ArrayList<DataFlowStatistics>();
        var segments = getSegmentsAsList();

        for (var bb : segments) {
            for (var repetition : repetitions) {
                var dfg = new BasicBlockDataFlowGraph(bb, repetition);
                if (useAGUs) {
                    System.out.println("Modifying with AGUs");
                    SegmentAddressGenerationUnitAnalyzer.modifyWithAGUs(dfg);
                }
                var dep = new DependencyGraph(dfg, useDependencies);

                var stats = new DataFlowStatistics(dep, dfg.getSegment());
                stats.setInsts(bb).setRepetitions(repetition);

                System.out.println(
                        "Scheduling a segment of " + this.elf.getFilename() + " with " + repetition + " repetitions");
                for (var aluN : alus) {
                    for (var memPortsN : memPorts) {
                        var scheduler = new ListScheduler(dep);
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