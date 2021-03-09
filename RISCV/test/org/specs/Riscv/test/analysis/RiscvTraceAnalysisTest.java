package org.specs.Riscv.test.analysis;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.Riscv.RiscvGccELF;
import org.specs.Riscv.RiscvLivermoreELFN100iam;
import org.specs.Riscv.stream.RiscvDetailedTraceProvider;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.analysis.InOutAnalysis;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class RiscvTraceAnalysisTest {

    private RiscvTraceStream getStream(ELFProvider file) {
        var fd = BinaryTranslationUtils.getFile(file);
        RiscvDetailedTraceProvider prod = new RiscvDetailedTraceProvider(fd);
        var stream = new RiscvTraceStream(prod);
        return stream;
    }

    
    @Test
    public void testRiscvDetailedProvider() {
        var fd = BinaryTranslationUtils.getFile(RiscvGccELF.autocor);
        var prov = new RiscvDetailedTraceProvider(fd);
        Instruction i = prov.nextInstruction();
        while (i != null) {
            i.printInstruction();
            i = prov.nextInstruction();
        }
        prov.close();
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
