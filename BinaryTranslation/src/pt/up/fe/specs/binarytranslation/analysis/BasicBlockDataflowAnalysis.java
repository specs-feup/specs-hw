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

import pt.up.fe.specs.binarytranslation.analysis.dataflow.BasicBlockDataFlow;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowCriticalPath;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class BasicBlockDataflowAnalysis extends ATraceAnalyzer {

    public BasicBlockDataflowAnalysis(ATraceInstructionStream stream, ELFProvider elf) {
        super(stream, elf);
    }

    public void analyze(List<Integer> windows) {
        AnalysisUtils.printSeparator(40);
        var n = windows.size();
        var instNumbers = new ArrayList<Integer>();
        
        for (var i : windows) {
            var res = analyze(i);
            System.out.println(res.toString());
            instNumbers.add(res.getInsts().size());
        }
        
        double mean = 0;
        for (var i : instNumbers)
            mean += i;
        mean = mean / n;
        
        double std = 0;
        for (var i : instNumbers)
            std += Math.pow(i - mean, 2);
        std = std / n;
        std = Math.sqrt(std);
        
        AnalysisUtils.printSeparator(40);
        System.out.println(AnalysisUtils.padRight("Total Basic Blocks found: ", 30) + n);
        System.out.println(AnalysisUtils.padRight("No. inst. Mean: ", 30) + mean);
        System.out.println(AnalysisUtils.padRight("No. inst. Standard deviation: ", 30) + std);
    }

    public DataFlowStatistics analyze(int window) {
        var det = buildDetector(window);
        List<BinarySegment> segs = AnalysisUtils.getSegments(stream, det);
        List<Instruction> insts = det.getProcessedInsts();
        var bb = segs.get(0);

        var tracker = new BasicBlockOccurrenceTracker(bb, insts);
        var bbdf = new BasicBlockDataFlow(tracker);
        var dfg = bbdf.buildDFG();

        var pathfinder = new DataFlowCriticalPath(dfg);
        var path = pathfinder.calculatePath();

        var sources = new ArrayList<String>();
        var sinks = new ArrayList<String>();
        for (var v : pathfinder.findSinks()) {
            if (v.getType() == AddressVertexType.REGISTER)
                sinks.add(v.getLabel());
        }
        for (var v : pathfinder.findSources()) {
            if (v.getType() == AddressVertexType.REGISTER)
                sources.add(v.getLabel());
        }

        var res = new DataFlowStatistics(dfg, path, bb.getInstructions(), sources, sinks);
        return res;
    }
}
