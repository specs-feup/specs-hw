package org.specs.Riscv.test.stream;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreELFN100iam;
import org.specs.Riscv.stream.RiscvElfStream;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTestUtils;

public class RiscvElfStreamTester {

    @Test
    public void testStatic() {
        InstructionStreamTestUtils.printStream(
                new RiscvElfStream(RiscvLivermoreELFN100iam.innerprod100));
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTestUtils.rawDump(
                new RiscvElfStream(RiscvLivermoreELFN100iam.innerprod100));
    }

    @Test
    public void testStaticRawFromTxtDump() {
        InstructionStreamTestUtils.rawDump(
                new RiscvElfStream(RiscvLivermoreELFN100iam.innerprod100.asTxtDump()));
    }

    @Test
    public void testTrace() {
        InstructionStreamTestUtils.printStream(
                new RiscvTraceStream(RiscvLivermoreELFN100iam.innerprod100));
    }

    @Test
    public void testTraceRaw() {
        InstructionStreamTestUtils.rawDump(
                new RiscvTraceStream(RiscvLivermoreELFN100iam.innerprod100));
    }

    @Test
    public void testTraceRawFromTxtDump() {
        InstructionStreamTestUtils.rawDump(
                new RiscvTraceStream(RiscvLivermoreELFN100iam.innerprod100.asTraceTxtDump()));
    }
}
