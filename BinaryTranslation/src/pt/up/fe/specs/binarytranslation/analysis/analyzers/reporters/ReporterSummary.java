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
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class ReporterSummary extends AReporter {

    public ReporterSummary(Map<ZippedELFProvider, Integer[]> elfWindows, Class streamClass) {
        super(elfWindows, streamClass);
        // TODO Auto-generated constructor stub
    }

    public ReporterSummary(Map<ZippedELFProvider, List<List<Instruction>>> staticBlocks) {
        super(staticBlocks);
    }

    @Override
    protected void processResults(ArrayList<DataFlowStatistics> results, String prefix) {
        var benchCSV = new StringBuilder("Benchmark,Basic Block ID, #Instructions, #Operations, #Memory accesses, Mean, STD\n");
        var dfgCsv = new StringBuilder("Benchmark,Graph\n");
        var stats = new HashMap<String, List<Integer>>();

        for (var res : results) {
            var key = res.getElfName();

            if (stats.containsKey(key)) {
                stats.get(key).add(res.getInsts().size());
            } else {
                var l = new ArrayList<Integer>();
                l.add(res.getInsts().size());
                stats.put(key, l);
            }
            dfgCsv.append(res.getElfName() + "_" + res.getId() + "," + res.getGraphAsDot() + "\n");
        }
        AnalysisUtils.saveAsCsv(dfgCsv, "results_new/graphs");

        var total = new ArrayList<Integer>();
        var benchMean = new HashMap<String, Double>();
        var benchSTD = new HashMap<String, Double>();
        
        for (var key : stats.keySet()) {
            var bbs = stats.get(key);
            var mean = arithmeticMean(bbs);
            var std = standardDeviation(bbs, mean);
            
            benchMean.put(key, mean);
            benchSTD.put(key, std);

            total.addAll(bbs);
        }
        
        for (var res : results) {
            benchCSV.append(res.getElfName()).append(",")
            .append(res.getId()).append(",")
            .append(res.getInsts().size()).append(",")
            .append(getOperations(res)).append(",")
            .append(getMemoryOps(res)).append(",")
            .append(benchMean.get(res.getElfName())).append(",")
            .append(benchSTD.get(res.getElfName())).append("\n");
        }

        var totalMean = arithmeticMean(total);
        var totalStd = standardDeviation(total, totalMean);
        benchCSV.append("Total,--,--,--,--,").append(totalMean).append(",")
                .append(totalStd).append("\n");

        AnalysisUtils.saveAsCsv(benchCSV, "results/dataFlowBenchmark" + prefix);
    }

    private int getOperations(DataFlowStatistics res) {
        var cnt = 0;
        for (var v : res.getGraph().vertexSet()) {
            if (v.getType() == BtfVertexType.OPERATION)
                cnt++;
        }
        return cnt;
    }

    private int getMemoryOps(DataFlowStatistics res) {
        var cnt = 0;
        for (var v : res.getGraph().vertexSet()) {
            if (v.getType() == BtfVertexType.MEMORY)
                cnt++;
        }
        return cnt;
    }

    private double arithmeticMean(List<Integer> samples) {
        double mean = 0;

        for (var s : samples)
            mean += s;
        mean = mean / samples.size();
        return mean;
    }

    private double standardDeviation(List<Integer> samples, double mean) {
        double std = 0;
        for (var s : samples) {
            std += Math.pow(s - mean, 2);
        }
        std = std / samples.size();
        return std;
    }

    @Override
    protected List<DataFlowStatistics> analyzeStatic(int repetitions, List<Instruction> block) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<DataFlowStatistics> analyzeStream(int repetitions, ZippedELFProvider elf, int window,
            ATraceInstructionStream stream) {
        return analyzeStream(repetitions, elf, window, stream);
    }

    @Override
    protected List<DataFlowStatistics> analyzeStream(int[] repetitions, ZippedELFProvider elf, int window,
            ATraceInstructionStream stream) {
        return analyzeStream(repetitions[0], elf, window, stream);
    }

}
