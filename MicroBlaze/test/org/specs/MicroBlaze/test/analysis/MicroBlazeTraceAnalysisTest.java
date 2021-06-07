package org.specs.MicroBlaze.test.analysis;

import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.MicroBlaze.MicroBlazeGccOptimizationLevels;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.MicroBlazePolyBenchMedium;
import org.specs.MicroBlaze.MicroBlazeRosetta;
import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.asm.MicroBlazeRegisterConventions;
import org.specs.MicroBlaze.stream.MicroBlazeDetailedTraceProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.analysis.BasicBlockDataflowAnalysis;
import pt.up.fe.specs.binarytranslation.analysis.BtfPerformanceAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.InOutAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.MemoryAccessTypesAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.MemoryAddressAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.MemoryProfilerAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.StreamingAnalysis;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.BasicBlockDataFlow;
import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.templates.GraphTemplateFactory;
import pt.up.fe.specs.binarytranslation.analysis.memory.templates.GraphTemplateReport;
import pt.up.fe.specs.binarytranslation.analysis.memory.templates.GraphTemplateType;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

public class MicroBlazeTraceAnalysisTest {

    private MicroBlazeTraceStream getStream(ELFProvider file, boolean generate) {
        String f = file.getResource();
        if (!generate) {
            f = ((MicroBlazeLivermoreELFN10) file).asTraceTxtDump();
        }
        File fd = SpecsIo.resourceCopy(f);
        fd.deleteOnExit();
        var prod = new MicroBlazeDetailedTraceProvider(fd);
        var stream = new MicroBlazeTraceStream(prod);
        return stream;
    }

    @Test
    public void testMemoryProfilerStream() {
        var elf = MicroBlazeLivermoreELFN10.linrec;
        int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.innerprod; int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;

        // var elf = MicroBlazeLivermoreELFN100.matmul100; int window = 15;

        var stream = getStream(MicroBlazeLivermoreELFN10.innerprod, false);
        var mem = new MemoryProfilerAnalyzer(stream, elf);
        assertTrue(mem.profile(true));
    }

    @Test
    public void testDetailedStream() {
        var stream = getStream(MicroBlazeLivermoreELFN10.pic2d, false);

        System.out.println("Testing detailed stream");
        Instruction inst = stream.nextInstruction();
        int regYes = 0;
        int regNo = 0;

        while (inst != null) {
            if (inst.getRegisters().isEmpty())
                regNo++;
            else
                regYes++;
            inst = stream.nextInstruction();
        }
        System.out.println("End");
        System.out.println("With regs: " + regYes + ", no regs: " + regNo);
        stream.close();
    }

    @Test
    public void testInstPerSecondNoRegisters() {
        System.out.println("--- Without registers ---");
        var fd1 = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        var prod1 = new MicroBlazeTraceProvider(fd1);
        var perf1 = new BtfPerformanceAnalyzer(prod1);
        perf1.calcInstructionsPerSecond();
    }

    @Test
    public void testInstPerSecondWithRegisters() {
        System.out.println("--- With registers ---");
        var fd2 = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        var prod2 = new MicroBlazeDetailedTraceProvider(fd2);
        var perf2 = new BtfPerformanceAnalyzer(prod2);
        perf2.calcInstructionsPerSecond();
    }

    @Test
    public void testBasicBlockInOuts() {
        var elf = MicroBlazeLivermoreELFN10.linrec;
        int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.innerprod; int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;

        // var elf = MicroBlazeLivermoreELFN100.matmul100; int window = 15;

        var fd = BinaryTranslationUtils.getFile(elf);
        var stream = new MicroBlazeTraceStream(fd);
        InOutAnalyzer bba = new InOutAnalyzer(stream, elf);
        bba.analyse(InOutAnalyzer.InOutMode.BASIC_BLOCK, window);
    }

    @Test
    public void testTraceInOuts() {
        var elf = MicroBlazeLivermoreELFN10.linrec;
        int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.innerprod; int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;

        // var elf = MicroBlazeLivermoreELFN100.matmul100; int window = 15;

        var fd = BinaryTranslationUtils.getFile(elf);
        var stream = new MicroBlazeTraceStream(fd);
        InOutAnalyzer bba = new InOutAnalyzer(stream, elf);
        bba.analyse(InOutAnalyzer.InOutMode.TRACE, window);
    }

    @Test
    public void testSimpleBasicBlockInOuts() {
        var elf = MicroBlazeLivermoreELFN10.linrec;
        int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.innerprod; int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;

        // var elf = MicroBlazeLivermoreELFN100.matmul100; int window = 15;

        var fd = BinaryTranslationUtils.getFile(elf);
        var stream = new MicroBlazeTraceStream(fd);
        InOutAnalyzer bba = new InOutAnalyzer(stream, elf);
        bba.analyse(InOutAnalyzer.InOutMode.SIMPLE_BASIC_BLOCK, window);
    }

    @Test
    public void testBasicBlockElimination() {
        // var elf = MicroBlazeLivermoreELFN10.linrec; int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.innerprod; int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;
        var elf = MicroBlazeLivermoreELFN10.state_frag;
        int window = 31;

        // var elf = MicroBlazeLivermoreELFN100.matmul100; int window = 15;

        var fd = BinaryTranslationUtils.getFile(elf);
        var stream = new MicroBlazeTraceStream(fd);
        InOutAnalyzer bba = new InOutAnalyzer(stream, elf);
        bba.analyse(InOutAnalyzer.InOutMode.ELIMINATION, window);
    }

    @Test
    public void testDumpTrace() throws FileNotFoundException {
        // var elf = MicroBlazeGccOptimizationLevels.autocor2;
        // var elf = MicroBlazeGccOptimizationLevels.dotprod2;
        var elf = MicroBlazeGccOptimizationLevels.fir3;

        // for (var elf : MicroBlazeLivermoreELFN10.values()) {
        // if (elf == MicroBlazeLivermoreELFN10.cholesky_trace || elf == MicroBlazeLivermoreELFN10.pic2d)
        // continue;
        var f = elf.asTraceTxtDump();
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(f))));
        var stream = getStream(elf, true);
        stream.rawDump();
        stream.close();
        System.err.println("Saved trace of " + elf.getFilename());
        // }
    }

    @Test
    public void testAddresses() {
        // Livermore N10
        var elf = MicroBlazeLivermoreELFN10.linrec;
        int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.innerprod; int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;
        // var elf = MicroBlazeLivermoreELFN10.state_frag; int window = 31;

        // Livermore N100
        // var elf = MicroBlazeLivermoreELFN100.matmul100; int window = 15;

        // GCC optimization levels
        // var elf = MicroBlazeGccOptimizationLevels.test2; int window = 22;
        // var elf = MicroBlazeGccOptimizationLevels.fir1; int window = 12;
        // var elf = MicroBlazeGccOptimizationLevels.firparam2; int window = 10;

        var fd = BinaryTranslationUtils.getFile(elf);
        var stream = new MicroBlazeTraceStream(fd);
        var maa = new MemoryAddressAnalyzer(stream, elf, new MicroBlazeRegisterConventions());
        maa.analyze(window);
    }

    @Test
    public void testMemoryAccessTypes() {
         var elfs = Map.of(
         MicroBlazeLivermoreELFN10.linrec, 10,
         MicroBlazeLivermoreELFN10.innerprod, 10,
         MicroBlazeLivermoreELFN10.hydro, 14,
         MicroBlazeLivermoreELFN10.cholesky, 18,
         //MicroBlazeLivermoreELFN10.hydro2d, 17,
         MicroBlazeLivermoreELFN10.tri_diag, 11,
         MicroBlazeLivermoreELFN10.state_frag, 31
         );
//        var elfs = Map.of(
//                MicroBlazeLivermoreELFN100.matmul100, 15);
        var reports = new ArrayList<GraphTemplateReport>();

        for (var elf : elfs.keySet()) {
            var window = elfs.get(elf);
            var fd = BinaryTranslationUtils.getFile(elf.asTraceTxtDump());
            var stream = new MicroBlazeTraceStream(fd);
            var analyzer = new MemoryAccessTypesAnalyzer(stream, elf);
            var rep = analyzer.analyze(window);
            rep.setName(elf.name());
            reports.add(rep);
        }

//        System.out.println("--------------------------");
//        for (var temp : GraphTemplateType.values()) {
//            var url = GraphUtils.generateGraphURL(GraphTemplateFactory.getTemplate(temp).toString());
//            System.out.println(temp + ": " + url);
//        }
        
        var sb = new StringBuilder();
        for (var r : reports) {
            sb.append(r.toString());
        }
        System.out.println("--------------------------");
        System.out.println(sb.toString());
        System.out.println("--------------------------");
        
        var csv = new File("patterns.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csv))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            SpecsLogs.warn("Error message:\n", e);
        }
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
        var analyzer = new StreamingAnalysis(stream, elf);
        analyzer.analyze(window, 10);
    }

    @Test
    public void findBasicBlocks() {
        //var elf = MicroBlazePolyBenchMedium.adi;
        var elf = MicroBlazeRosetta.facedetection;

        int minwindow = 4;
        int maxwindow = 200;

        for (int i = minwindow; i <= maxwindow; i++) {
            var fd = BinaryTranslationUtils.getFile(elf.asTraceTxtDump());
            var istream1 = new MicroBlazeTraceStream(fd);
            istream1.silent(true);
            // istream1.advanceTo(elf.getKernelStart().longValue());

            System.out.println("Looking for segments of size: " + i);

            var detector1 = new TraceBasicBlockDetector(// new FrequentTraceSequenceDetector(
                    new DetectorConfigurationBuilder()
                            .withMaxWindow(i)
                            .withStartAddr(elf.getKernelStart())
                            .withStopAddr(elf.getKernelStop())
                            .withPrematureStopAddr(elf.getKernelStop().longValue())
                            .build());
            var result1 = detector1.detectSegments(istream1);
            if (result1.getSegments().size() == 0)
                continue;

            // var gbundle = GraphBundle.newInstance(result1);
            // gbundle.generateOutput();
            SegmentDetectTestUtils.printBundle(result1);
        }
    }
    
    @Test
    public void testBasicBlockDataFlow() {
        //var elf = MicroBlazeLivermoreELFN10.linrec; int window = 10;
        //var elf = MicroBlazeLivermoreELFN10.innerprod; int window = 10;
        //var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        //var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        //var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;
        //var elf = MicroBlazeLivermoreELFN10.state_frag; int window = 31;

        var fd = BinaryTranslationUtils.getFile(elf.asTraceTxtDump());
        var stream = new MicroBlazeTraceStream(fd);
        var analyzer = new BasicBlockDataflowAnalysis(stream, elf);
        analyzer.analyze(Arrays.asList(new Integer[] {window}));
    }
}
