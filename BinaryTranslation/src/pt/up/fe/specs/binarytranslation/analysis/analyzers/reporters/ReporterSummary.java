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
import java.util.Map;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;

public class ReporterSummary extends AReporter {

    public ReporterSummary(Map<ELFProvider, Integer[]> elfWindows, Class streamClass) {
        super(elfWindows, streamClass);
        // TODO Auto-generated constructor stub
    }

    public ReporterSummary(Map<ELFProvider, List<List<Instruction>>> staticBlocks) {
        super(staticBlocks);
    }

    @Override
    protected void processResults(ArrayList<DataFlowStatistics> results, String prefix) {
        // TODO Auto-generated method stub

    }

    @Override
    protected List<DataFlowStatistics> analyzeStatic(int repetitions, List<Instruction> block) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<DataFlowStatistics> analyzeStream(int repetitions, ELFProvider elf, int window,
            TraceInstructionStream stream) {
        // TODO Auto-generated method stub
        return null;
    }

}
