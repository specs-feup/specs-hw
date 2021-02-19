package org.specs.Riscv.test.stream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

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
                RiscvLivermoreELFN100iam.innerprod100.getResource(), RiscvTraceStream.class);
        // RiscvLivermoreELFN100iamf.innerprod100.getResource(), RiscvTraceStream.class);
    }

    @Test
    public void testTraceRaw() {
        InstructionStreamTestUtils.rawDump(
                RiscvLivermoreELFN100iam.innerprod100.getResource(), RiscvTraceStream.class);
    }
    
}
