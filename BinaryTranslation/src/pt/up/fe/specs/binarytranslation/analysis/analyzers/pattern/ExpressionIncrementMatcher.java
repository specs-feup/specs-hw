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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class ExpressionIncrementMatcher extends APatternAnalyzer {
    private List<String> elfNames = new ArrayList<>();
    private List<String> bbID = new ArrayList<>();
    private List<String> memID = new ArrayList<>();
    private List<String> registers = new ArrayList<>();

    public ExpressionIncrementMatcher(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        super(stream, elf, window);
    }

    private void matchReports(MemoryPatternReport memRep, LoopIncrementReport incRep) {
        System.out.println(memRep.getBasicBlockIDs());
        System.err.println(incRep.getBasicBlockIDs());
        System.out.println("----------");
    }

    @Override
    protected APatternReport matchTemplates(List<BinarySegment> segs) {
        var memAn = new MemoryAccessTypesAnalyzer(stream, elf, window);
        var memRep = (MemoryPatternReport) memAn.matchWithPrecalculatedBlocks(segs);

        var incAn = new LoopIncrementPatternAnalyzer(stream, elf, window);
        var incRep = (LoopIncrementReport) incAn.matchWithPrecalculatedBlocks(segs);

        var name = elf.getELFName();
        memRep.setName(name);
        incRep.setName(name);

        matchReports(memRep, incRep);
        return null;
    }

}
