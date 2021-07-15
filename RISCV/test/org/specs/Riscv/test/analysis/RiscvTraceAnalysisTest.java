package org.specs.Riscv.test.analysis;

import java.io.File;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.Riscv.RiscvGccELF;
import org.specs.Riscv.RiscvLivermoreELFN100iam;
import org.specs.Riscv.stream.RiscvDetailedTraceProvider;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.analysis.analyzers.InOutAnalyzer;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.InOutAnalyzer.InOutMode;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.SpecsIo;

public class RiscvTraceAnalysisTest {

    private RiscvTraceStream getStream(ELFProvider file, boolean generate) {
        String f = file.getResource();
        if (!generate) {
            f = ((RiscvLivermoreELFN100iam) file).asTraceTxtDump();
        }
        File fd = SpecsIo.resourceCopy(f);
        fd.deleteOnExit();
        var prod = new RiscvDetailedTraceProvider(fd);
        var stream = new RiscvTraceStream(prod);
        return stream;
    }

    @Test
    public void testRiscvDetailedProvider() {
        var fd = BinaryTranslationUtils.getFile(RiscvGccELF.autocor);
        try (var prov = new RiscvDetailedTraceProvider(fd)) {
            Instruction i = prov.nextInstruction();
            while (i != null) {
                i.printInstruction();
                i = prov.nextInstruction();
            }
        }
    }

    @Test
    public void testRiscvInOuts() {
        // var elf = RiscvLivermoreELFN100iam.innerprod100;
//        var elf = RiscvGccELF.autocor;
//        var stream = getStream(elf, true);
//        var bba = new InOutAnalyzer(stream, elf);
//        bba.analyse(InOutMode.BASIC_BLOCK, 10);
    }
}
