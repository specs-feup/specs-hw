package org.specs.Riscv.test.stream;

import org.junit.Test;
import org.specs.Riscv.Riscv32IMAFPolybench;
import org.specs.Riscv.RiscvLivermoreELFN100iam;
import org.specs.Riscv.RiscvLivermoreELFN100iamf;
import org.specs.Riscv.stream.RiscvElfStream;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTestUtils;

public class RiscvElfStreamTester {

    @Test
    public void testStatic() {
        InstructionStreamTestUtils.printStream(
                Riscv32IMAFPolybench.gemm, RiscvElfStream.class);
        // RiscvLivermoreELFN100iamf.innerprod100, RiscvElfStream.class);
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTestUtils.rawDump(
                Riscv32IMAFPolybench.gemm, RiscvElfStream.class);
        // RiscvLivermoreELFN100iamf.innerprod100, RiscvElfStream.class);
    }

    @Test
    public void testTrace() {
        InstructionStreamTestUtils.printStream(
                // Riscv32IMAFPolybench.gemm, RiscvTraceStream.class);
                RiscvLivermoreELFN100iam.innerprod100, RiscvTraceStream.class);
        // RiscvLivermoreELFN100iamf.innerprod100.getResource(), RiscvTraceStream.class);
    }

    @Test
    public void testTraceRaw() {
        InstructionStreamTestUtils.rawDump(
                // Riscv32IMAFPolybench.gemm, RiscvTraceStream.class);
                RiscvLivermoreELFN100iamf.cholesky100, RiscvTraceStream.class);
    }

}
