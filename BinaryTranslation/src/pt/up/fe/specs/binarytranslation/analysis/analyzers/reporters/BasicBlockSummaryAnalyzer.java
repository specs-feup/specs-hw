/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.analysis.analyzers.reporters;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.ASegmentAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class BasicBlockSummaryAnalyzer extends ASegmentAnalyzer {

    public BasicBlockSummaryAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        super(stream, elf, window);
    }
    
    public BasicBlockSummaryAnalyzer(List<List<Instruction>> basicBlocks) {
        super(basicBlocks);
    }
    
    public List<DataFlowStatistics> analyze() {
        var res = new ArrayList<DataFlowStatistics>();
        var segments = getSegmentsAsList();
        
        for (var bb : segments) {
            var dfg = new BasicBlockDataFlowGraph(bb, 1);
            var stats = new DataFlowStatistics(dfg);
            res.add(stats);
        }
        return res;
    }

}
