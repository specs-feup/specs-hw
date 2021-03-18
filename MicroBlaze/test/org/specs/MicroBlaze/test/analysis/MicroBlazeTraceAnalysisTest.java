package org.specs.MicroBlaze.test.analysis;

import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeDetailedTraceProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.analysis.BtfPerformanceAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.InOutAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.InductionVariableAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.MemoryProfilerAnalyzer;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.SpecsIo;

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
        var stream = getStream(MicroBlazeLivermoreELFN10.innerprod, false);
        var mem = new MemoryProfilerAnalyzer(stream);
        assertTrue(mem.profileWithStream());
    }

    @Test
    public void testMemoryProfilerProducer() {
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        var prod = new MicroBlazeDetailedTraceProvider(fd);
        var mem = new MemoryProfilerAnalyzer(prod);
        assertTrue(mem.profileWithProducer());
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
        // No basic blocks detected with these!
        // var elf = MicroBlazeGccOptimizationLevels.autocor2;
        // var elf = MicroBlazeGccOptimizationLevels.dotprod2;
        // var elf = MicroBlazeLivermoreELFN10.matmul;
        // var elf = MicroBlazeLivermoreELFN10.hydro;
        // var elf = MicroBlazeLivermoreELFN10.diffpredict;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag;
        // var elf = MicroBlazeLivermoreELFN10.pic2d;

        var elf = MicroBlazeLivermoreELFN10.innerprod;
        // var elf = MicroBlazeLivermoreELFN10.linrec;
        var stream = getStream(elf, false);

        InOutAnalyzer bba = new InOutAnalyzer(stream);
        bba.analyse(InOutAnalyzer.InOutMode.BASIC_BLOCK);
    }

    @Test
    public void testTraceInOuts() {
        // var elf = MicroBlazeLivermoreELFN10.innerprod;
        var elf = MicroBlazeLivermoreELFN10.linrec;
        var stream = getStream(elf, false);

        InOutAnalyzer bba = new InOutAnalyzer(stream);
        bba.analyse(InOutAnalyzer.InOutMode.TRACE);
    }

    @Test
    public void testSimpleBasicBlockInOuts() {
        // No basic blocks detected with these!
        // var elf = MicroBlazeGccOptimizationLevels.autocor2;
        // var elf = MicroBlazeGccOptimizationLevels.dotprod2;
        // var elf = MicroBlazeLivermoreELFN10.matmul;
        // var elf = MicroBlazeLivermoreELFN10.hydro;
        // var elf = MicroBlazeLivermoreELFN10.diffpredict;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag;
        // var elf = MicroBlazeLivermoreELFN10.pic2d;

        var elf = MicroBlazeLivermoreELFN10.innerprod;
        // var elf = MicroBlazeLivermoreELFN10.linrec;
        var stream = getStream(elf, false);

        InOutAnalyzer bba = new InOutAnalyzer(stream);
        bba.analyse(InOutAnalyzer.InOutMode.SIMPLE_BASIC_BLOCK);
    }

    @Test
    public void testBasicBlockElimination() {
        // var elf = MicroBlazeGccOptimizationLevels.autocor2;
        // var elf = MicroBlazeGccOptimizationLevels.dotprod2;
        // var elf = MicroBlazeLivermoreELFN10.matmul;
        // var elf = MicroBlazeLivermoreELFN10.hydro;
        // var elf = MicroBlazeLivermoreELFN10.diffpredict;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag;
        // var elf = MicroBlazeLivermoreELFN10.pic2d;

        // var elf = MicroBlazeLivermoreELFN10.cholesky;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d;
        // var elf = MicroBlazeLivermoreELFN10.hydro2dimpl;
        // var elf = MicroBlazeLivermoreELFN10.intpredict;
        // var elf = MicroBlazeLivermoreELFN10.pic1d;

        // var elf = MicroBlazeLivermoreELFN10.innerprod;
        var elf = MicroBlazeLivermoreELFN10.linrec;
        var stream = getStream(elf, false);

        InOutAnalyzer bba = new InOutAnalyzer(stream);
        bba.analyse(InOutAnalyzer.InOutMode.ELIMINATION);
    }

    @Test
    public void testDumpTrace() throws FileNotFoundException {
        // var elf = MicroBlazeGccOptimizationLevels.autocor2;
        // var elf = MicroBlazeGccOptimizationLevels.dotprod2;

        for (var elf : MicroBlazeLivermoreELFN10.values()) {
            if (elf == MicroBlazeLivermoreELFN10.cholesky_trace || elf == MicroBlazeLivermoreELFN10.pic2d)
                continue;
            var f = elf.asTraceTxtDump();
            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(f))));
            var stream = getStream(elf, true);
            stream.rawDump();
            stream.close();
            System.err.println("Saved trace of " + elf.getFilename());
        }
    }

    @Test
    public void testInductionVariableDetection() {
        var elf = MicroBlazeLivermoreELFN10.innerprod;
        // var elf = MicroBlazeLivermoreELFN10.linrec;
        var stream = getStream(elf, false);

        InductionVariableAnalyzer ivd = new InductionVariableAnalyzer(stream);
        ivd.analyze();
    }
}
