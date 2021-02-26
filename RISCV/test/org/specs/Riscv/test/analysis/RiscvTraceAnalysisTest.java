package org.specs.Riscv.test.analysis;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.specs.Riscv.RiscvLivermoreELFN100iam;
import org.specs.Riscv.stream.detailed.RiscvDetailedTraceProvider;

import pt.up.fe.specs.binarytranslation.analysis.TraceAnalysis;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class RiscvTraceAnalysisTest {

    @Test
    public void testRiscVAnalysis() {
        var fd = BinaryTranslationUtils.getFile(RiscvLivermoreELFN100iam.innerprod100);
        var dip = new RiscvDetailedTraceProvider(fd);
        var ta = new TraceAnalysis(dip);
        assertTrue(ta.analyze());
    }
}
