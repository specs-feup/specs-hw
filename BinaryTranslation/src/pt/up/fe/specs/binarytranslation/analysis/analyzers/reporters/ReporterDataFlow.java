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
import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class ReporterDataFlow extends AReporter {

    public ReporterDataFlow(Map<ZippedELFProvider, Integer[]> elfWindows, HashMap<ZippedELFProvider, HashMap<Integer, ATraceInstructionStream>> streams) {
        super(elfWindows, streams);
    }

    public ReporterDataFlow(Map<ZippedELFProvider, List<List<Instruction>>> staticBlocks) {
        super(staticBlocks);
    }

    @Override
    public void processResults(ArrayList<DataFlowStatistics> results, String prefix) {
        var basicBlockCSV = new StringBuilder(
                "Benchmark,Basic Block ID,#Inst,Critical Path Size,ILP Measure,Pairs,Graph\n");

        for (var res : results) {
            basicBlockCSV.append(res.getElfName()).append(",").append(res.getId()).append(",")
                    .append(res.getInsts().size()).append(",").append(res.getPathSize()).append(",")
                    .append(res.getILP()).append(",").append(res.getPairs()).append(",")
                    .append(res.getGraphAsDot()).append("\n");
            System.out.println(res.getGraphAsDot());
        }

        AnalysisUtils.saveAsCsv(basicBlockCSV, "results/basicBlockFlowBenchmark" + prefix);

    }

    @Override
    public List<DataFlowStatistics> analyzeStatic(int repetitions, List<Instruction> block) {
        // TODO Auto-generated method stub
        return null;
    }

    
    private List<DataFlowStatistics> analyzeStream(int repetitions, ZippedELFProvider elf, int window, ATraceInstructionStream stream) {
        var analyzer = new BasicBlockDataFlowAnalyzer(stream, elf, window);
        var resList = analyzer.analyze(repetitions);
        return resList;
    }

    @Override
    protected List<DataFlowStatistics> analyzeStream(int[] repetitions, ZippedELFProvider elf, int window,
            ATraceInstructionStream stream) {
        return analyzeStream(repetitions[0], elf, window, stream);
    }
}
