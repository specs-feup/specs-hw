package org.specs.traceanalysis.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeDetailedTraceProvider;
import org.specs.Riscv.RiscvLivermoreELFN100iam;
import org.specs.Riscv.stream.detailed.RiscvDetailedTraceProvider;
import org.specs.traceanalysis.TraceAnalysis;

import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class TraceAnalysisTest {

    @Test
    public void testRiscVAnalysis() {
        var fd = BinaryTranslationUtils.getFile(RiscvLivermoreELFN100iam.innerprod100);
        var dip = new RiscvDetailedTraceProvider(fd);
        var ta = new TraceAnalysis(dip);
        assertTrue(ta.analyze());
    }

    @Test
    public void testMicroBlazeAnalysis() {
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        var dip = new MicroBlazeDetailedTraceProvider(fd);
        var ta = new TraceAnalysis(dip);
        assertTrue(ta.analyze());
    }
}
