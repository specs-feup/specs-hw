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

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowStatistics;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.SpecsLogs;

public abstract class AReporter {
    private Map<ELFProvider, Integer[]> elfWindows;
    protected Class streamClass;
    private Map<ELFProvider, List<List<Instruction>>> staticBlocks;
    private boolean isStatic = false;

    public AReporter(Map<ELFProvider, Integer[]> elfWindows, Class streamClass) {
        this.elfWindows = elfWindows;
        this.streamClass = streamClass;
    }

    public AReporter(Map<ELFProvider, List<List<Instruction>>> staticBlocks) {
        this.staticBlocks = staticBlocks;
        this.isStatic = true;
    }

    public void analyze(int repetition, String prefix) {
        analyze(new int[] { repetition }, prefix);
    }

    public void analyze(int[] repetitions, String prefix) {
        var results = new ArrayList<DataFlowStatistics>();

        for (var repetition : repetitions) {
            if (isStatic) {
                staticHandler(repetition, results);
            } else {
                streamHandler(repetition, results);
            }
        }

        processResults(results, prefix);
    }

    private void streamHandler(int repetitions, ArrayList<DataFlowStatistics> results) {
        for (var elf : elfWindows.keySet()) {
            int id = 1;
            for (var window : elfWindows.get(elf)) {
                // var fd = BinaryTranslationUtils.getFile(elf.asTraceTxtDump());
                var fd = elf.getFile();
                Constructor cons;
                ATraceInstructionStream stream = null;
                try {
                    cons = streamClass.getConstructor(File.class);
                    stream = (ATraceInstructionStream) cons.newInstance(fd);
                } catch (Exception e) {
                    SpecsLogs.warn("Error message:\n", e);
                }

                var res = analyzeStream(repetitions, elf, window, stream);
                id = processResult(results, elf, id, res);
            }
        }
    }

    private void staticHandler(int repetitions, ArrayList<DataFlowStatistics> results) {
        for (var elf : staticBlocks.keySet()) {
            int id = 1;
            for (var block : staticBlocks.get(elf)) {
                var res = analyzeStatic(repetitions, block);
                id = processResult(results, elf, id, res);
            }
        }
    }

    private int processResult(ArrayList<DataFlowStatistics> results, ELFProvider elf, int id,
            List<DataFlowStatistics> res) {
        for (var r : res) {
            r.setId("BB" + id);
            r.setElfName(elf.toString());
            id++;
        }
        results.addAll(res);
        return id;
    }

    protected abstract void processResults(ArrayList<DataFlowStatistics> results, String prefix);

    protected abstract List<DataFlowStatistics> analyzeStatic(int repetitions, List<Instruction> block);

    protected abstract List<DataFlowStatistics> analyzeStream(int repetitions, ELFProvider elf, int window,
            ATraceInstructionStream stream);
}
