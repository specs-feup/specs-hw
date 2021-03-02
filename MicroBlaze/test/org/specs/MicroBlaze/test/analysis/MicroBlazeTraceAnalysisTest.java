package org.specs.MicroBlaze.test.analysis;

import static org.junit.Assert.assertFalse;
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

    private MicroBlazeDetailedTraceProvider getProvider(ELFProvider file) {
        var fd = BinaryTranslationUtils.getFile(file);
        var prod = new MicroBlazeDetailedTraceProvider(fd);
        return prod;
    }

    @Test
    public void testMemoryProfiler() {
        var prod = getProvider(MicroBlazeLivermoreELFN10.innerprod);
        var mem = new MemoryProfiler(prod);
        assertTrue(mem.profile());
    }

    @Test
    public void testDetailedStream() {
        var prod = getProvider(MicroBlazeLivermoreELFN10.innerprod);
        var stream = new MicroBlazeTraceStream(prod);
        int heartbeat = 0;
        //stream.rawDump();
        
        System.out.println("Testing detailed stream");
        Instruction inst = stream.nextInstruction();
        int regYes = 0;
        int regNo = 0;
        
        while (inst != null) {
            if (inst.getRegisters().isEmpty())
                regNo++;
            else
                regYes++;
            heartbeat++;
            if (heartbeat % 1000 == 0)
                System.out.println("Still alive (inst=" + heartbeat + ")");
            inst = stream.nextInstruction();
        }
        System.out.println("End");
        System.out.println("With regs: " + regYes + ", no regs: " + regNo);
        stream.close();
    }

    @Test
    public void testInstPerSecondNoRegisters() {
        System.out.println("--- Without registers ---");
        var prod1 = new MicroBlazeTraceProvider(BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod));
        var perf1 = new BtfPerformance(prod1);
        perf1.calcInstructionsPerSecond();
    }

    @Test
    public void testInstPerSecondWithRegisters() {
        System.out.println("--- With registers ---");
        var prod2 = getProvider(MicroBlazeLivermoreELFN10.innerprod);
        var perf2 = new BtfPerformance(prod2);
        perf2.calcInstructionsPerSecond();
    }
    
    @Test
    public void testBasicBlockDetection() {
        var prod = getProvider(MicroBlazeLivermoreELFN10.innerprod);
        var stream = new MicroBlazeTraceStream(prod);
        
        BasicBlockAnalyser bba = new BasicBlockAnalyser(stream);
        bba.analyse();
    }
    
    @Test
    public void testDumpTrace() throws FileNotFoundException {
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("dump.txt"))));
        var prod = getProvider(MicroBlazeLivermoreELFN10.innerprod);
        var stream = new MicroBlazeTraceStream(prod);
        stream.rawDump();
        stream.close();
    }
}
