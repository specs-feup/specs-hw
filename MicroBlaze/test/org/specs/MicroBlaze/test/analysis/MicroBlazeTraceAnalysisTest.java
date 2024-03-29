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

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeGccOptimizationLevels;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN10;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProducer;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.analysis.analyzers.BtfPerformanceAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.InOutAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.MemoryAddressAnalyzer;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class MicroBlazeTraceAnalysisTest {
    /*
    private MicroBlazeTraceStream getStream(ELFProvider file, boolean generate) {
        String f = file.getResource();
        if (!generate) {
            f = ((MicroBlazeLivermoreELFN10) file).asTraceTxtDump();
        }
        File fd = SpecsIo.resourceCopy(f);
        fd.deleteOnExit();
        var prod = new MicroBlazeDetailedTraceProducer(fd);
        var stream = new MicroBlazeTraceStream(prod);
        return stream;
    }*/

    // private MicroBlazeTraceStream getStream(MicroBlazeELFProvider elfprovider, boolean generate) {
    // var prod = new MicroBlazeDetailedTraceProducer(new MicroBlazeTraceDumpProvider(elfprovider));
    // var stream = new MicroBlazeTraceStream(prod);
    // return stream;
    // }

    // @Test
    // public void testMemoryProfilerStream() {
    // var elf = MicroBlazeLivermoreELFN10.linrec;
    // int window = 10;
    // // var elf = MicroBlazeLivermoreELFN10.innerprod; int window = 10;
    // // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
    // // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
    // // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
    // // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;
    //
    // // var elf = MicroBlazeLivermoreELFN100.matmul100; int window = 15;
    //
    // var stream = getStream(MicroBlazeLivermoreELFN10.innerprod, false);
    // var mem = new MemoryProfilerAnalyzer(stream, elf);
    // assertTrue(mem.profile(true));
    // }

    @Test
    public void testDetailedStream() {
        // var stream = getStream(MicroBlazeLivermoreN10.pic2d, false);
        try (var stream = MicroBlazeLivermoreN10.pic2d.toTraceStream()) {

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

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testInstPerSecondNoRegisters() {
        System.out.println("--- Without registers ---");
        var prod1 = new MicroBlazeTraceProducer(MicroBlazeLivermoreN10.innerprod);
        var perf1 = new BtfPerformanceAnalyzer(prod1);
        perf1.calcInstructionsPerSecond();
    }

    @Test
    public void testInstPerSecondWithRegisters() {
        System.out.println("--- With registers ---");
        // var prod2 = new MicroBlazeDetailedTraceProducer(MicroBlazeLivermoreN10.innerprod);
        var prod2 = new MicroBlazeTraceProducer(MicroBlazeLivermoreN10.innerprod);
        var perf2 = new BtfPerformanceAnalyzer(prod2);
        perf2.calcInstructionsPerSecond();
    }

    @Test
    public void testBasicBlockInOuts() {
        var elf = MicroBlazeLivermoreN10.linrec;
        int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.innerprod; int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;

        // var elf = MicroBlazeLivermoreELFN100.matmul100; int window = 15;

        var stream = new MicroBlazeTraceStream(elf);
        InOutAnalyzer bba = new InOutAnalyzer(stream, elf, window);
        bba.analyse(InOutAnalyzer.InOutMode.BASIC_BLOCK);
    }

    @Test
    public void testTraceInOuts() {
        var elf = MicroBlazeLivermoreN10.linrec;
        int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.innerprod; int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;

        // var elf = MicroBlazeLivermoreELFN100.matmul100; int window = 15;

        var stream = new MicroBlazeTraceStream(elf);
        InOutAnalyzer bba = new InOutAnalyzer(stream, elf, window);
        bba.analyse(InOutAnalyzer.InOutMode.TRACE);
    }

    @Test
    public void testSimpleBasicBlockInOuts() {
        var elf = MicroBlazeLivermoreN10.linrec;
        int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.innerprod; int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;

        // var elf = MicroBlazeLivermoreELFN100.matmul100; int window = 15;

        var stream = new MicroBlazeTraceStream(elf);
        InOutAnalyzer bba = new InOutAnalyzer(stream, elf, window);
        bba.analyse(InOutAnalyzer.InOutMode.SIMPLE_BASIC_BLOCK);
    }

    @Test
    public void testBasicBlockElimination() {
        // var elf = MicroBlazeLivermoreELFN10.linrec; int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.innerprod; int window = 10;
        // var elf = MicroBlazeLivermoreELFN10.hydro; int window = 14;
        // var elf = MicroBlazeLivermoreELFN10.cholesky; int window = 18;
        // var elf = MicroBlazeLivermoreELFN10.hydro2d; int window = 17;
        // var elf = MicroBlazeLivermoreELFN10.tri_diag; int window = 11;
        var elf = MicroBlazeLivermoreN10.state_frag;
        int window = 31;

        // var elf = MicroBlazeLivermoreELFN100.matmul100; int window = 15;

        var stream = new MicroBlazeTraceStream(elf);
        InOutAnalyzer bba = new InOutAnalyzer(stream, elf, window);
        bba.analyse(InOutAnalyzer.InOutMode.ELIMINATION);
    }

    @Test
    public void testDumpTrace() throws FileNotFoundException {
        // var elf = MicroBlazeGccOptimizationLevels.autocor2;
        var elf = MicroBlazeGccOptimizationLevels.dotprod2;
        // var elf = MicroBlazePolyBenchBLASSmall.twomm;

        // for (var elf : MicroBlazeLivermoreELFN10.values()) {
        // if (elf == MicroBlazeLivermoreELFN10.cholesky_trace || elf ==
        // MicroBlazeLivermoreELFN10.pic2d)
        // continue;
        var f = elf.asTraceTxtDump().write();
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(f))));

        // var stream = getStream(elf, true);
        // stream.rawDump();
        // stream.close();

        try (var stream = MicroBlazeGccOptimizationLevels.dotprod2.toTraceStream()) {
            stream.rawDump();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.err.println("Saved trace of " + elf.getFilename());
        // }
    }

    @Test
    public void testAddresses() {
        // Livermore N10
        var elf = MicroBlazeLivermoreN10.linrec;
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

        var stream = new MicroBlazeTraceStream(elf);
        var maa = new MemoryAddressAnalyzer(stream, elf, window);
        maa.analyze();
    }
}
