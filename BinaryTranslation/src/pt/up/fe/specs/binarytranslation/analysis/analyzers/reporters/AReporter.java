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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegmentType;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public abstract class AReporter {
    private Map<ZippedELFProvider, Integer[]> elfWindows;
    private Map<ZippedELFProvider, List<List<Instruction>>> staticBlocks;
    private boolean isStatic = false;
    private HashMap<ZippedELFProvider, HashMap<Integer, ATraceInstructionStream>> streams;
    protected BinarySegmentType segmentType = BinarySegmentType.TRACE_BASIC_BLOCK;

    public AReporter(Map<ZippedELFProvider, Integer[]> elfWindows, HashMap<ZippedELFProvider, HashMap<Integer, ATraceInstructionStream>> streams) {
        this.elfWindows = elfWindows;
        this.streams = streams;
    }
    
    public AReporter(Map<ZippedELFProvider, Integer[]> elfWindows, HashMap<ZippedELFProvider, HashMap<Integer, ATraceInstructionStream>> streams, BinarySegmentType type) {
        this.elfWindows = elfWindows;
        this.streams = streams;
        this.segmentType = type;
    }

    public AReporter(Map<ZippedELFProvider, List<List<Instruction>>> staticBlocks) {
        this.staticBlocks = staticBlocks;
        this.isStatic = true;
    }

    public void analyze() {
        analyze(new int[] { 1 });
    }
    

    public void analyze(int[] repetitions) {
        var results = new ArrayList<DataFlowStatistics>();

        if (isStatic) {
            staticHandler(repetitions, results);
        } else {
            streamHandler(repetitions, results);
        }

        processResults(results);
    }

    private void streamHandler(int[] repetitions, ArrayList<DataFlowStatistics> results) {
        for (var elf : elfWindows.keySet()) {
            int id = 1;
            for (var window : elfWindows.get(elf)) {
                List<DataFlowStatistics> res = new ArrayList<>();
                var stream = streams.get(elf).get(window);
                
                res = analyzeStream(repetitions, elf, window, stream);
                if (res.size() == 0)
                    continue;
                id = processResult(results, elf, id, res, repetitions);
            }
        }
    }

    private void staticHandler(int[] repetitions, ArrayList<DataFlowStatistics> results) {
        // for (var elf : staticBlocks.keySet()) {
        // int id = 1;
        // for (var block : staticBlocks.get(elf)) {
        // var res = analyzeStatic(repetitions, block);
        // id = processResult(results, elf, id, res);
        // }
        // }
    }

    private int processResult(ArrayList<DataFlowStatistics> results, ZippedELFProvider elf, int id,
            List<DataFlowStatistics> res, int[] repetitions) {
        var cnt = 0;
        
        for (var r : res) {
            r.setId("BB" + id);
            r.setElfName(elf.toString());
            cnt++;
            
            if (cnt == repetitions[repetitions.length - 1]) {
                id++;
                cnt = 0;
            }
        }
        results.addAll(res);
        return id;
    }

    protected abstract void processResults(ArrayList<DataFlowStatistics> results);

    protected abstract List<DataFlowStatistics> analyzeStatic(int repetitions, List<Instruction> block);

    protected abstract List<DataFlowStatistics> analyzeStream(int[] repetitions, ZippedELFProvider elf, int window,
            ATraceInstructionStream stream);

    protected BinarySegmentType getSegmentType() {
        return segmentType;
    }

    protected void setSegmentType(BinarySegmentType segmentType) {
        this.segmentType = segmentType;
    }
}
