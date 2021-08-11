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
import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;

public class ReporterScheduling extends AReporter {

    private int[] alus;
    private int[] memPorts;

    public ReporterScheduling(Map<ELFProvider, Integer[]> elfWindows, Class streamClass, int[] alus, int[] memPorts) {
        super(elfWindows, streamClass);
        this.alus = alus;
        this.memPorts = memPorts;
    }

    public ReporterScheduling(Map<ELFProvider, List<List<Instruction>>> staticBlocks, int[] alus, int[] memPorts) {
        super(staticBlocks);
        this.alus = alus;
        this.memPorts = memPorts;
    }

    @Override
    protected void processResults(ArrayList<DataFlowStatistics> results, String prefix) {
        var csv = new StringBuilder("Benchmark,BasicBlock,Unroll,");
        var toJoin = new ArrayList<String>();
        for (var a : alus) {
            for (var m : memPorts) {
                toJoin.add("[" + a + " | " + m + "]");
            }
        }
        csv.append(String.join(",", toJoin)).append("\n");

        for (var res : results) {
            csv.append(res.getElfName()).append(",")
                    .append(res.getId()).append(",")
                    .append(res.getRepetitions());
            for (var s : res.getSchedules()) {
                csv.append(",").append(s);
            }
            csv.append("\n");
        }

        AnalysisUtils.saveAsCsv(csv, "results/scheduling" + prefix);
    }

    @Override
    protected List<DataFlowStatistics> analyzeStatic(int repetitions, List<Instruction> block) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DataFlowStatistics> analyzeStream(int repetitions, ELFProvider elf, int window,
            TraceInstructionStream stream) {
        var analyzer = new BasicBlockSchedulingAnalyzer(stream, elf, window);
        var resList = analyzer.analyze(repetitions, alus, memPorts);
        return resList;
    }

}
