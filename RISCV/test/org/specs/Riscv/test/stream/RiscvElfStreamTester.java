package org.specs.Riscv.test.stream;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100im;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTester;

public class RiscvElfStreamTester extends InstructionStreamTester {

    @Test
    public void testStatic() {
        printStream(RiscvLivermoreN100im.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRaw() {
        rawDump(RiscvLivermoreN100im.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRawFromTxtDump() {
        rawDump(RiscvLivermoreN100im.innerprod.asTxtDump().toStaticStream());
    }

    @Test
    public void testTrace() {
        printStream(RiscvLivermoreN100im.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRaw() {
        rawDump(RiscvLivermoreN100im.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRawFromTxtDump() {
        rawDump(RiscvLivermoreN100im.innerprod.asTraceTxtDump().toTraceStream());
    }
}
