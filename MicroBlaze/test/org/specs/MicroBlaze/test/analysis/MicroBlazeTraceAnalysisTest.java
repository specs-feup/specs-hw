package org.specs.MicroBlaze.test.analysis;

import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeDetailedTraceProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.analysis.BasicBlockAnalyser;
import pt.up.fe.specs.binarytranslation.analysis.BtfPerformance;
import pt.up.fe.specs.binarytranslation.analysis.MemoryProfiler;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeTraceAnalysisTest {

    private MicroBlazeTraceStream getStream(ELFProvider file) {
        var fd = BinaryTranslationUtils.getFile(file);
        var prod = new MicroBlazeDetailedTraceProvider(fd);
        var stream = new MicroBlazeTraceStream(prod);
        return stream;
    }

    @Test
    public void testMemoryProfilerStream() {
        var stream = getStream(MicroBlazeLivermoreELFN10.innerprod);
        var mem = new MemoryProfiler(stream);
        assertTrue(mem.profileWithStream());
    }
    
    @Test
    public void testMemoryProfilerProducer() {
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        var prod = new MicroBlazeDetailedTraceProvider(fd);
        var mem = new MemoryProfiler(prod);
        assertTrue(mem.profileWithProducer());
    }

    @Test
    public void testDetailedStream() {
        var stream = getStream(MicroBlazeLivermoreELFN10.innerprod);
        
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
        var perf1 = new BtfPerformance(prod1);
        perf1.calcInstructionsPerSecond();
    }

    @Test
    public void testInstPerSecondWithRegisters() {
        System.out.println("--- With registers ---");
        var fd2 = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        var prod2 = new MicroBlazeDetailedTraceProvider(fd2);
        var perf2 = new BtfPerformance(prod2);
        perf2.calcInstructionsPerSecond();
    }
    
    @Test
    public void testBasicBlockDetection() {
        var stream = getStream(MicroBlazeLivermoreELFN10.innerprod);
        
        BasicBlockAnalyser bba = new BasicBlockAnalyser(stream);
        bba.analyse();
    }
    
    @Test
    public void testDumpTrace() throws FileNotFoundException {
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("dump.txt"))));
        var stream = getStream(MicroBlazeLivermoreELFN10.innerprod);
        stream.rawDump();
        stream.close();
    }
}
