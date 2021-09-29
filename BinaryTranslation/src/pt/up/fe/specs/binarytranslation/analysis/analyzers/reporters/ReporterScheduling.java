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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.SpecsLogs;

public class ReporterScheduling extends AReporter {

    private int[] alus;
    private int[] memPorts;
    private boolean useDependencies;

    public ReporterScheduling(Map<ZippedELFProvider, Integer[]> elfWindows, HashMap<ZippedELFProvider, HashMap<Integer, ATraceInstructionStream>> streams, int[] alus, int[] memPorts,
            boolean useDependencies) {
        super(elfWindows, streams);
        this.alus = alus;
        this.memPorts = memPorts;
        this.useDependencies = useDependencies;
    }

    public ReporterScheduling(Map<ZippedELFProvider, List<List<Instruction>>> staticBlocks, int[] alus, int[] memPorts,
            boolean useDependencies) {
        super(staticBlocks);
        this.alus = alus;
        this.memPorts = memPorts;
        this.useDependencies = useDependencies;
    }

    public ReporterScheduling(Map<ZippedELFProvider, Integer[]> elfWindows, HashMap<ZippedELFProvider, HashMap<Integer, ATraceInstructionStream>> streams, List<Integer> alus,
            List<Integer> memPorts, boolean useDependencies) {
        super(elfWindows, streams);
        this.alus = new int[alus.size()];
        this.memPorts = new int[memPorts.size()];
        for (int i = 0; i < alus.size(); i++)
            this.alus[i] = alus.get(i);
        for (int i = 0; i < memPorts.size(); i++)
            this.memPorts[i] = memPorts.get(i);
        this.useDependencies = useDependencies;
    }

    // @Override
    // protected void processResults(ArrayList<DataFlowStatistics> results, String prefix) {
    // var csv = new StringBuilder("Benchmark,BasicBlock,Unroll,");
    // var toJoin = new ArrayList<String>();
    // for (var a : alus) {
    // for (var m : memPorts) {
    // toJoin.add("[" + a + " | " + m + "]");
    // }
    // }
    // csv.append(String.join(",", toJoin)).append("\n");
    //
    // for (var res : results) {
    // csv.append(res.getElfName()).append(",")
    // .append(res.getId()).append(",")
    // .append(res.getRepetitions());
    // for (var s : res.getSchedules()) {
    // csv.append(",").append(s);
    // }
    // csv.append("\n");
    // }
    //
    // AnalysisUtils.saveAsCsv(csv, "results/scheduling" + prefix);
    // generateAdditionalReport(results);
    // }

    @Override
    protected void processResults(ArrayList<DataFlowStatistics> results, String prefix) {
        var resultsPerBenchmark = getResultsPerBenchmark(results);
        var matrices = new ArrayList<BenchmarkSchedulingMatrix>();
        var dfgCsv = new StringBuilder("Benchmark,Repetitions,Graph\n");

        for (var bench : resultsPerBenchmark.keySet()) {
            var reports = resultsPerBenchmark.get(bench);
            var matrix = new BenchmarkSchedulingMatrix(reports, alus, memPorts);
            matrices.add(matrix);

            for (var report : reports) {
                dfgCsv.append(
                        matrix.getBenchmark() + "," + report.getRepetitions() + "," + report.getGraphAsDot() + "\n");
                for (var s : report.getSchedules()) {
                    var alus = s.getAluCount();
                    var mem = s.getMemPortCount();
                    AnalysisUtils.saveAsCsv(s.getFinalSchedule(),
                            "results_new/schedules/sched_" + matrix.getBenchmark() + "_rep_" + report.getRepetitions()
                                    + "_(" + alus + "_" + mem + ")");
                }
            }

            AnalysisUtils.saveAsCsv(matrix.getLatencyCsv(),
                    "results_new/latencies/sched_latency_" + matrix.getBenchmark());
            AnalysisUtils.saveAsCsv(matrix.getSpeedupCsv(),
                    "results_new/speedups/sched_speedup_" + matrix.getBenchmark());
        }
        AnalysisUtils.saveAsCsv(dfgCsv.toString(), "results_new/graphs");

        var header = matrices.get(0).getHeader();
        var reps = matrices.get(0).getRepetitions();
        var configs = matrices.get(0).getConfigs();
        var avgCsv = new StringBuilder(header);

        for (var rep : reps) {
            avgCsv.append(rep);
            for (var config : configs) {
                var avg = calculateAverageSpeedup(matrices, rep, config);
                avgCsv.append(",").append(String.format("%.2f", avg));
            }
            avgCsv.append("\n");
        }

        AnalysisUtils.saveAsCsv(avgCsv.toString(), "results_new/total_average_speedups");
    }

    private double calculateAverageSpeedup(ArrayList<BenchmarkSchedulingMatrix> matrices, Integer rep, String config) {
        double cnt = 0.0;
        for (var mat : matrices) {
            cnt += mat.getSpeedup(rep, config);
        }
        return cnt / matrices.size();
    }

    private Map<String, List<DataFlowStatistics>> getResultsPerBenchmark(ArrayList<DataFlowStatistics> results) {
        var output = new HashMap<String, List<DataFlowStatistics>>();

        for (var res : results) {
            String name = res.getElfName() + "_" + res.getId();
            if (!output.containsKey(name)) {
                output.put(name, new ArrayList<>());
            }
            output.get(name).add(res);
        }

        return output;
    }

    @Override
    protected List<DataFlowStatistics> analyzeStatic(int repetitions, List<Instruction> block) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DataFlowStatistics> analyzeStream(int[] repetitions, ZippedELFProvider elf, int window,
            ATraceInstructionStream stream) {
        var analyzer = new BasicBlockSchedulingAnalyzer(stream, elf, window, useDependencies);
        var resList = analyzer.analyze(repetitions, alus, memPorts);
        return resList;
    }
}
