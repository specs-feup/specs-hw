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

import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegmentType;
import pt.up.fe.specs.binarytranslation.elf.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class ReporterAddressGenerationUnit extends AReporter {

    public ReporterAddressGenerationUnit(Map<ZippedELFProvider, Integer[]> elfWindows,
            HashMap<ZippedELFProvider, HashMap<Integer, ATraceInstructionStream>> streams) {
        super(elfWindows, streams);
    }

    public ReporterAddressGenerationUnit(Map<ZippedELFProvider, Integer[]> elfWindows,
            HashMap<ZippedELFProvider, HashMap<Integer, ATraceInstructionStream>> streams, BinarySegmentType type) {
        super(elfWindows, streams, type);
    }

    public ReporterAddressGenerationUnit(Map<ZippedELFProvider, List<List<Instruction>>> staticBlocks) {
        super(staticBlocks);
    }

    @Override
    protected void processResults(ArrayList<DataFlowStatistics> results) {
        int i = 1;
        for (var res : results) {
            var s = GraphUtils.generateGraphURL(res.getGraph());
            System.out.println(i + ": " + s);
            i++;
            //System.out.println("------------------------------------");
        }
    }

    @Override
    protected List<DataFlowStatistics> analyzeStatic(int repetitions, List<Instruction> block) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<DataFlowStatistics> analyzeStream(int[] repetitions, ZippedELFProvider elf, int window,
            ATraceInstructionStream stream) {
        var aguAnalyzer = new SegmentAddressGenerationUnitAnalyzer(stream, elf, window, segmentType);
        var res = aguAnalyzer.analyze();
        return res;
    }

}
