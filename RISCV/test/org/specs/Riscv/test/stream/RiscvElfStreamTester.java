package org.specs.Riscv.test.stream;

import org.junit.Test;
import org.specs.Riscv.RiscvLivermoreELFN100iam;
import org.specs.Riscv.RiscvLivermoreELFN100iamf;
import org.specs.Riscv.stream.RiscvElfStream;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTestUtils;

public class RiscvElfStreamTester {

    @Test
    public void testStatic() {
        InstructionStreamTestUtils.printStream(
                // RiscvLivermoreELFN100iam.innerprod100.getResource(), RiscvElfStream.class);
                RiscvLivermoreELFN100iamf.innerprod100.getResource(), RiscvElfStream.class);
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTestUtils.rawDump(
                RiscvLivermoreELFN100iam.innerprod100.getResource(), RiscvElfStream.class);
    }

    @Test
    public void testTrace() {
        InstructionStreamTestUtils.printStream(
                // "org/specs/Riscv/utils/test64.elf", RiscvTraceStream.class);
                // RiscvLivermoreELFN100iam.innerprod100.getResource(), RiscvTraceStream.class);
                RiscvLivermoreELFN100iamf.innerprod100.getResource(), RiscvTraceStream.class);
    }

    @Test
    public void testTraceRaw() {
        InstructionStreamTestUtils.rawDump(
                // "org/specs/Riscv/utils/test64.elf", RiscvTraceStream.class);
                RiscvLivermoreELFN100iam.innerprod100.getResource(), RiscvTraceStream.class);
    }
}
