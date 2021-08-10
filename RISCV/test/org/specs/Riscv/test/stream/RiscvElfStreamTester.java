package org.specs.Riscv.test.stream;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreELFN100iam;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTester;

public class RiscvElfStreamTester extends InstructionStreamTester {

    @Test
    public void testStatic() {
        printStream(RiscvLivermoreELFN100iam.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRaw() {
        rawDump(RiscvLivermoreELFN100iam.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRawFromTxtDump() {
        rawDump(RiscvLivermoreELFN100iam.innerprod.asTxtDump().toStaticStream());
    }

    @Test
    public void testTrace() {
        printStream(RiscvLivermoreELFN100iam.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRaw() {
        rawDump(RiscvLivermoreELFN100iam.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRawFromTxtDump() {
        rawDump(RiscvLivermoreELFN100iam.innerprod.asTraceTxtDump().toTraceStream());
    }
}
