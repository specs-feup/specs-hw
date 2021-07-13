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

package pt.up.fe.specs.binarytranslation.analysis.analyzers;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.BasicBlockDataflowAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.SpecsLogs;

public class KernelDataFlowAnalyzer extends AKernelAnalyzer {

    public KernelDataFlowAnalyzer(Map<ELFProvider, Integer[]> elfWindows, Class streamClass) {
        super(elfWindows, streamClass);
    }

    public KernelDataFlowAnalyzer(Map<ELFProvider, List<List<Instruction>>> staticBlocks) {
        super(staticBlocks);
    }

    @Override
    public void processResults(ArrayList<DataFlowStatistics> results, String prefix) {
        var basicBlockCSV = new StringBuilder(
                "Benchmark,Basic Block ID,#Inst,Critical Path Size,ILP Measure,Pairs,Graph\n");
        //var benchCSV = new StringBuilder("Benchmark,#BasicBlocks,#Inst Mean,#Inst STD\n");

        for (var res : results) {
            basicBlockCSV.append(res.getElfName()).append(",").append(res.getId()).append(",")
                    .append(res.getInsts().size()).append(",").append(res.getPathSize()).append(",")
                    .append(res.getILP()).append(",").append(res.getPairs()).append(",")
                    .append(res.getGraphAsDot()).append("\n");
        }

        // double mean = 0;
        // for (var i : instNumbers)
        // mean += i;
        // mean = mean / n;
        //
        // double std = 0;
        // for (var i : instNumbers)
        // std += Math.pow(i - mean, 2);
        // std = std / n;
        // std = Math.sqrt(std);
        //
        // benchCSV.append(elf.getResourceName()).append(",").append(n).append(",").append(mean).append(",")
        // .append(std)
        // .append("\n");

        // Save as CSV
        //AnalysisUtils.saveAsCsv(benchCSV, "results/dataFlowBenchmark" + filename);
        AnalysisUtils.saveAsCsv(basicBlockCSV, "results/basicBlockFlowBenchmark" + prefix);

    }

    @Override
    public List<DataFlowStatistics> analyzeStatic(int repetitions, List<Instruction> block) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DataFlowStatistics> analyzeStream(int repetitions, ELFProvider elf, int window) {
        var fd = BinaryTranslationUtils.getFile(elf.asTraceTxtDump());
        Constructor cons;
        ATraceInstructionStream stream = null;
        try {
            cons = streamClass.getConstructor(File.class);
            stream = (ATraceInstructionStream) cons.newInstance(fd);
        } catch (Exception e) {
            SpecsLogs.warn("Error message:\n", e);
        }

        var analyzer = new BasicBlockDataflowAnalyzer(stream, elf);
        var resList = analyzer.analyzeWithDetector(window, repetitions);
        return resList;
    }
}
