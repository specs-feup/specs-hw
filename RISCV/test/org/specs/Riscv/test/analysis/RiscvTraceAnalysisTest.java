package org.specs.Riscv.test.analysis;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvELFProvider;
import org.specs.Riscv.provider.RiscvGccELF;
import org.specs.Riscv.provider.RiscvTraceDumpProvider;
import org.specs.Riscv.stream.RiscvDetailedTraceProducer;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class RiscvTraceAnalysisTest {

    /*  private RiscvTraceStream getStream(ELFProvider file, boolean generate) {
        String f = file.getResource();
        if (!generate) {
            f = ((RiscvLivermoreELFN100iam) file).asTraceTxtDump();
        }
        File fd = SpecsIo.resourceCopy(f);
        fd.deleteOnExit();
        var prod = new RiscvDetailedTraceProvider(fd);
        var stream = new RiscvTraceStream(prod);
        return stream;
    }*/

    private RiscvTraceStream getStream(RiscvELFProvider elfprovider, boolean generate) {
        var prod = new RiscvDetailedTraceProducer(new RiscvTraceDumpProvider(elfprovider));
        var stream = new RiscvTraceStream(prod);
        return stream;
    }

    @Test
    public void testRiscvDetailedProvider() {
        try (var prov = new RiscvDetailedTraceProducer(RiscvGccELF.autocor)) {
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
        // var elf = RiscvGccELF.autocor;
        // var stream = getStream(elf, true);
        // var bba = new InOutAnalyzer(stream, elf);
        // bba.analyse(InOutMode.BASIC_BLOCK, 10);
    }
}
