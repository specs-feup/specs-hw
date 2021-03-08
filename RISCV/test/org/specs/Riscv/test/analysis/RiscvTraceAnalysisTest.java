package org.specs.Riscv.test.analysis;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.Riscv.RiscvGccELF;
import org.specs.Riscv.RiscvLivermoreELFN100iam;
import org.specs.Riscv.stream.RiscvDetailedTraceProvider;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.analysis.basicblock.InOutAnalysis;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class RiscvTraceAnalysisTest {

    private RiscvTraceStream getStream(ELFProvider file) {
        var fd = BinaryTranslationUtils.getFile(file);
        RiscvDetailedTraceProvider prod = new RiscvDetailedTraceProvider(fd);
        var stream = new RiscvTraceStream(prod);
        return stream;
    }

    
    @Test
    public void testRiscVAnalysis() {
        var fd = BinaryTranslationUtils.getFile(RiscvLivermoreELFN100iam.innerprod100);
        var dip = new RiscvDetailedTraceProvider(fd);
    }
    
    @Test
    public void testRiscvInOuts() {
        //var elf = RiscvLivermoreELFN100iam.innerprod100;
        var elf = RiscvGccELF.autocor;
        var stream = getStream(elf);
        
        InOutAnalysis bba = new InOutAnalysis(stream);
        bba.analyse();
    }
}
