package org.specs.Riscv.test.stream;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreELFN100im;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTester;

public class RiscvElfStreamTester extends InstructionStreamTester {

    @Test
    public void testStatic() {
        printStream(RiscvLivermoreELFN100im.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRaw() {
        rawDump(RiscvLivermoreELFN100im.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRawFromTxtDump() {
        rawDump(RiscvLivermoreELFN100im.innerprod.asTxtDump().toStaticStream());
    }

    @Test
    public void testTrace() {
        printStream(RiscvLivermoreELFN100im.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRaw() {
        rawDump(RiscvLivermoreELFN100im.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRawFromTxtDump() {
        rawDump(RiscvLivermoreELFN100im.innerprod.asTraceTxtDump().toTraceStream());
    }
}
