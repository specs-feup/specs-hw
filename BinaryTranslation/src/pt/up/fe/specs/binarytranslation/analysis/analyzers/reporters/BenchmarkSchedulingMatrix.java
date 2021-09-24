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

public class BenchmarkSchedulingMatrix {
    private String benchmark;
    private Map<Integer, Map<String, Double>> latencyMat;
    private Map<Integer, Map<String, Double>> speedupMat;
    private List<Integer> repetitions = new ArrayList<>();
    private List<String> configs = new ArrayList<>();

    public BenchmarkSchedulingMatrix(List<DataFlowStatistics> reports, int[] alus, int[] memPorts) {
        benchmark = reports.get(0).getElfName() + "_" + reports.get(0).getId();
        latencyMat = new HashMap<Integer, Map<String, Double>>();
        speedupMat = new HashMap<Integer, Map<String, Double>>();
        configs.add("CPU");

        initLatencyMatrix(alus, memPorts, reports);
        initSpeedupMatrix();
    }

    private void initSpeedupMatrix() {
        for (var rep : repetitions) {
            var latencyRow = latencyMat.get(rep);
            var speedupRow = new HashMap<String, Double>();
            
            double cpuLatency = latencyRow.get("CPU");
            cpuLatency = cpuLatency / rep;
            
            for (var config : configs) {
                double latency = latencyRow.get(config) / rep;
                double speedup = cpuLatency / latency;
                speedupRow.put(config, speedup);
            }
            speedupMat.put(rep, speedupRow);
        }
    }

    private void initLatencyMatrix(int[] alus, int[] memPorts, List<DataFlowStatistics> reports) {
        for (var res : reports) {
            var rep = res.getRepetitions();
            repetitions.add(rep);

            var sched = res.getSchedules();
            var row = new HashMap<String, Double>();
            row.put("CPU", getCpuLatency(res));
            var i = 0;

            for (var a : alus) {
                for (var m : memPorts) {
                    String pair = "(" + a + "|" + m + ")";
                    if (!configs.contains(pair))
                        configs.add(pair);
                    row.put(pair, Double.valueOf(sched.get(i).getScheduleLatency()));
                    i++;
                }
            }
            latencyMat.put(rep, row);
        }
    }

    private Double getCpuLatency(DataFlowStatistics res) {
        var reps = res.getRepetitions();
        var insts = res.getInsts();
        double cnt = 0.0;
        for (var i : insts)
            cnt += i.getLatency();
        return cnt * reps;
    }

    public double getLatency(int repetition, String config) {
        var row = latencyMat.get(repetition);
        return row.get(config);
    }
    
    public double getSpeedup(int repetition, String config) {
        var row = speedupMat.get(repetition);
        if (row == null) {
            System.out.println("Null found...");
            return 0;
        }
        return row.get(config);
    }

    public String getLatencyCsv() {
        return getCsv(latencyMat);
    }
    
    public String getSpeedupCsv() {
        return getCsv(speedupMat);
    }
    
    private String getCsv(Map<Integer, Map<String, Double>> matrix) {
        StringBuilder sb = new StringBuilder(getHeader());
        
        for (var rep : repetitions) {
            var row = matrix.get(rep);
            sb.append(rep);
            for (var config : configs) {
                var n = String.format("%.2f", row.get(config));
                sb.append(",").append(n);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getBenchmark() {
        return benchmark;
    }

    public List<Integer> getRepetitions() {
        return repetitions;
    }

    public List<String> getConfigs() {
        return configs;
    }

    public Map<Integer, Map<String, Double>> getLatencyMat() {
        return latencyMat;
    }
    
    public Map<Integer, Map<String, Double>> getSpeedupMat() {
        return speedupMat;
    }

    public String getHeader() {
        var sb = new StringBuilder("Repetitions");
        for (var config : configs)
            sb.append(",").append(config);
        sb.append("\n");
        return sb.toString();
    }
}
