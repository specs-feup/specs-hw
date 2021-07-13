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

package pt.up.fe.specs.binarytranslation.analysis.analyzers;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.BasicBlockDataflowAnalyzer;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.SpecsLogs;

public class SchedulingAnalyzer <T extends ATraceInstructionStream> {
    private Map<ELFProvider, Integer[]> elfs;
    private Class<T> streamClass;

    public SchedulingAnalyzer(Map<ELFProvider, Integer[]> elfs, Class<T> streamClass) {
        this.elfs = elfs;
        this.streamClass = streamClass;
    }

    public void analyzeWithDetector(int repetitions, String filename, int[] alus, int[] memPorts) {
        try {
            analyze(repetitions, null, filename, alus, memPorts);
        } catch (Exception e) {
            SpecsLogs.warn("Error message:\n", e);
        }
    }
    
    public void analyzeWithStaticBasicBlock(int repetitions, List<List<Instruction>> basicBlocks, String filename, int[] alus, int[] memPorts) {
        try {
            analyze(repetitions, basicBlocks, filename, alus, memPorts);
        } catch (Exception e) {
            SpecsLogs.warn("Error message:\n", e);
        }
    }
    
    private void analyze(int repetitions, List<List<Instruction>> basicBlocks, String filename, int[] alus, int[] memPorts) throws Exception {
        var basicBlockCSV = new StringBuilder("Benchmark,Basic Block ID,#Inst,Critical Path Size,ILP Measure,Pairs,Graph\n");
        var benchCSV = new StringBuilder("Benchmark,#BasicBlocks,#Inst Mean,#Inst STD\n");

        for (var elf : elfs.keySet()) {
            AnalysisUtils.printSeparator(40);

            var windows = elfs.get(elf);
            var n = 0;
            var instNumbers = new ArrayList<Integer>();

            if (basicBlocks == null) {
                for (var i : windows) {
                    var fd = BinaryTranslationUtils.getFile(elf.asTraceTxtDump());
                    Constructor<T> cons = streamClass.getConstructor(File.class);
                    var stream = cons.newInstance(fd);
                    var analyzer = new BasicBlockDataflowAnalyzer(stream, elf);
                    
                    var resList = analyzer.analyzeWithDetector(i, repetitions, alus[0], memPorts[0]);
                    
                    for (var res : resList) {
                        System.out.println(res.toString());
                        instNumbers.add(res.getInsts().size());
                        n++;
                        basicBlockCSV.append(elf.getResourceName()).append(",").append("BB").append(n).append(",")
                        .append(res.getInsts().size()).append(",").append(res.getPathSize()).append(",")
                        .append(res.getILP())
                        .append(",").append(res.getPairs())
                        .append(",").append(res.getGraphAsDot()).append(",").append("\n");
                    }
                }
            }
            else {
                for (var bb : basicBlocks) {
                    var fd = BinaryTranslationUtils.getFile(elf.asTraceTxtDump());
                    Constructor<T> cons = streamClass.getConstructor(File.class);
                    var stream = cons.newInstance(fd);
                    var analyzer = new BasicBlockDataflowAnalyzer(stream, elf);
                    
                    var resList = analyzer.analyzeWithStaticBlock(bb, repetitions, alus[0], memPorts[0]);
                    
                    for (var res : resList) {
                        System.out.println(res.toString());
                        instNumbers.add(res.getInsts().size());
                        n++;
                        basicBlockCSV.append(elf.getResourceName()).append(",").append("BB").append(n).append(",")
                                .append(res.getInsts().size()).append(",").append(res.getPathSize()).append(",")
                                .append(res.getILP())
                                .append(",").append(res.getPairs())
                                .append(",").append(res.getGraphAsDot()).append(",").append("\n");
                    }
                }
            }

            double mean = 0;
            for (var i : instNumbers)
                mean += i;
            mean = mean / n;

            double std = 0;
            for (var i : instNumbers)
                std += Math.pow(i - mean, 2);
            std = std / n;
            std = Math.sqrt(std);

            benchCSV.append(elf.getResourceName()).append(",").append(n).append(",").append(mean).append(",").append(std)
                    .append("\n");
        }
//        AnalysisUtils.printSeparator(40);
//        System.out.print(basicBlockCSV.toString());
//        AnalysisUtils.printSeparator(40);
//        System.out.print(benchCSV.toString());
//        AnalysisUtils.printSeparator(40);

        // Save as CSV
        AnalysisUtils.saveAsCsv(benchCSV, "results/dataFlowBenchmark" + filename);
        AnalysisUtils.saveAsCsv(basicBlockCSV, "results/basicBlockFlowBenchmark" + filename);
    }

}
