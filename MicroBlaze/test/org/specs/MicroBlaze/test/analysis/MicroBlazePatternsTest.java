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

package org.specs.MicroBlaze.test.analysis;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN10;
import org.specs.MicroBlaze.provider.MicroBlazeTraceDumpProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.StreamingAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.MemoryPatternReport;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.ArithmeticExpressionMatcher;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.ArithmeticExpressionMatcher.ArithmeticTemplates;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.ArithmeticPatternReport;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.ExpressionIncrementMatchReport;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.ExpressionIncrementMatcher;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.LoopIncrementPatternAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.LoopIncrementReport;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.MemoryAccessTypesAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.graphs.templates.GraphTemplateType;

public class MicroBlazePatternsTest {

    @Test
    public void testMemoryAccessTypes() {
        var elfs = MicroBlazeBasicBlockInfo.getPolybenchMiniFloatKernels();
        var allReports = new ArrayList<MemoryPatternReport>();
        var allGraphs = new HashMap<String, String>();

        for (var elf : elfs.keySet()) {
            var windows = elfs.get(elf);
            int id = 1;

            for (var window : windows) {
                var stream = new MicroBlazeTraceStream(new MicroBlazeTraceDumpProvider((MicroBlazeELFProvider) elf));
                var analyzer = new MemoryAccessTypesAnalyzer(stream, elf, window);
                var name = elf.getELFName();
                var report = (MemoryPatternReport) analyzer.analyzeSegment();

                report.setName(name);
                allReports.add(report);
                allGraphs.put(name + "_" + id, report.getCompositeGraph());
            }
            allReports.get(0).resetLastID();
        }

        // Print templates
        System.out.println(GraphTemplateType.getAllTemplates());

        var sb = new StringBuilder();
        sb.append("Benchmark,Basic Block ID,Memory Access ID,Memory Access Type,#Occurrences,Graph\n");
        for (var r : allReports) {
            sb.append(r.toCsv());
        }
        AnalysisUtils.saveAsCsv(sb, "results/MemoryAccessPatterns");
    }

    @Test
    public void testLoopIncrement() {
        var elfs = MicroBlazeBasicBlockInfo.getPolybenchMiniFloatKernels();
        var allReports = new ArrayList<LoopIncrementReport>();

        for (var elf : elfs.keySet()) {
            var windows = elfs.get(elf);
            var name = elf.getELFName();

            for (var window : windows) {
                var stream = new MicroBlazeTraceStream(new MicroBlazeTraceDumpProvider((MicroBlazeELFProvider) elf));
                var analyzer = new LoopIncrementPatternAnalyzer(stream, elf, window);
                var report = (LoopIncrementReport) analyzer.analyzeSegment();

                report.setName(name);
                allReports.add(report);
            }
            allReports.get(0).resetLastID();
        }

        var sb = new StringBuilder();
        sb.append("Benchmark,Basic Block ID,Type,Register,Constant,Graph Single Iter,Graph Double Iter\n");
        for (var r : allReports) {
            sb.append(r.toCsv());
        }
        AnalysisUtils.saveAsCsv(sb, "results/LoopIncrements");
    }

    @Test
    public void testExpressionIncrementMatcher() {
        var elfs = MicroBlazeBasicBlockInfo.getLivermoreN100Kernels();
        var allReports = new ArrayList<ExpressionIncrementMatchReport>();

        for (var elf : elfs.keySet()) {
            var windows = elfs.get(elf);
            var name = elf.getELFName();

            for (var window : windows) {
                var stream = new MicroBlazeTraceStream(new MicroBlazeTraceDumpProvider((MicroBlazeELFProvider) elf));
                var analyzer = new ExpressionIncrementMatcher(stream, elf, window);
                var report = (ExpressionIncrementMatchReport) analyzer.analyzeSegment();

                report.setName(name);
                allReports.add(report);
            }
            (new MemoryPatternReport("")).resetLastID();
            (new LoopIncrementReport()).resetLastID();
        }

        var sb = new StringBuilder();
        sb.append("Benchmark,BBID,MemExpr ID,Registers,Matches\n");
        for (var r : allReports) {
            sb.append(r.toCsv());
        }
        AnalysisUtils.saveAsCsv(sb, "results/ExpressionIncrementMatches");
    }
    
    @Test
    public void testArithmeticExpressions() {
        var elfs = MicroBlazeBasicBlockInfo.getLivermoreN100Kernels();
        var allReports = new ArrayList<ArithmeticPatternReport>();
        int reps = 1;

        for (var elf : elfs.keySet()) {
            var windows = elfs.get(elf);
            var name = elf.getELFName();

            for (var window : windows) {
                var stream = new MicroBlazeTraceStream(new MicroBlazeTraceDumpProvider((MicroBlazeELFProvider) elf));
                var analyzer = new ArithmeticExpressionMatcher(stream, elf, window, reps);
                var report = (ArithmeticPatternReport) analyzer.analyzeSegment();

                report.setName(name);
                allReports.add(report);
            }
            allReports.get(0).resetLastID();
        }

        var sb = new StringBuilder();
        sb.append("Benchmark,Basic Block ID");
        for (var t : ArithmeticTemplates.values()) {
            sb.append(",").append(t.toString());
        }
        sb.append("\n");
        for (var r : allReports) {
            sb.append(r.toCsv());
        }
        AnalysisUtils.saveAsCsv(sb, "results/ArithmeticExpressions");
    }

    
    @Test
    public void testStreaming() {
        // var elf = MicroBlazeLivermoreELFN10.linrec; int window = 10;
        var elf = MicroBlazeLivermoreN10.innerprod;
        int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;
        // var elf = MicroBlazeLivermoreELFN10.state_frag; int window = 31;

        var stream = new MicroBlazeTraceStream(new MicroBlazeTraceDumpProvider((MicroBlazeELFProvider) elf));
        var analyzer = new StreamingAnalyzer(stream, elf, window);
        analyzer.analyze(10);
    }
}
