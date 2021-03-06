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

package org.specs.MicroBlaze.test.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.MemoryAccessTypesAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.StreamingAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.GraphTemplateReport;
import pt.up.fe.specs.binarytranslation.analysis.graphs.templates.GraphTemplateType;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeMemoryPatternsTest {
    @Test
    public void testMemoryAccessTypes() {
        var elfs = MicroBlazeBasicBlockInfo.getPolybenchSmallFloatKernels();
        
        var allReports = new ArrayList<GraphTemplateReport>();
        var allGraphs = new HashMap<String, String>();

        for (var elf : elfs.keySet()) {
            var windows = elfs.get(elf);
            int id = 1;

            for (var window : windows) {
                var fd = BinaryTranslationUtils.getFile(elf.asTraceTxtDump());
                var stream = new MicroBlazeTraceStream(fd);
                var analyzer = new MemoryAccessTypesAnalyzer(stream, elf, window);
                var name = elf.getResourceName();
                var report = analyzer.analyzeSegment();

                report.setName(name);
                allReports.add(report);
                allGraphs.put(name + "_" + id, report.getCompositeGraph());
            }
        }

        // Print templates
        System.out.println(GraphTemplateType.getAllTemplates());

        // Print graph of each BB
        for (var key : allGraphs.keySet())
            System.out.println(key + ": " + allGraphs.get(key));

        // Print report
        var sb = new StringBuilder();
        sb.append("Benchmark,Basic Block ID,Memory Access ID,Memory Access Type,#Occurrences,Graph\n");
        for (var r : allReports) {
            sb.append(r.toString());
        }
        System.out.println("--------------------------");
        System.out.println(sb.toString());
        System.out.println("--------------------------");

        // Save as CSV
        AnalysisUtils.saveAsCsv(sb, "BasicBlockPatterns");
    }

    @Test
    public void testStreaming() {
        // var elf = MicroBlazeLivermoreELFN10.linrec; int window = 10;
        var elf = MicroBlazeLivermoreELFN10.innerprod;
        int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;
        // var elf = MicroBlazeLivermoreELFN10.state_frag; int window = 31;

        var fd = BinaryTranslationUtils.getFile(elf.asTraceTxtDump());
        var stream = new MicroBlazeTraceStream(fd);
        var analyzer = new StreamingAnalyzer(stream, elf, window);
        analyzer.analyze(10);
    }
}
