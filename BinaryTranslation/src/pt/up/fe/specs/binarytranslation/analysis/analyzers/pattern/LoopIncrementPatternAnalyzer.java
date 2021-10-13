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

package pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern;

import java.util.List;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;
import pt.up.fe.specs.binarytranslation.analysis.graphs.templates.GraphTemplateType;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class LoopIncrementPatternAnalyzer extends APatternAnalyzer {

    public LoopIncrementPatternAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        super(stream, elf, window);
    }

    @Override
    protected APatternReport matchTemplates(List<BinarySegment> segs) {
        var report = new LoopIncrementReport();

        for (var bb : segs) {
            var insts = bb.getInstructions();
            var dfg1 = new BasicBlockDataFlowGraph(insts, 1);
            var dfg2 = new BasicBlockDataFlowGraph(insts, 2);

            report.addEntry(dfg1, dfg2);
        }
        return report;
    }

}
