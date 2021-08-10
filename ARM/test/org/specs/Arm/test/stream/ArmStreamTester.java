package org.specs.Arm.test.stream;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreELFN10;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTester;

public class ArmStreamTester extends InstructionStreamTester {

    @Test
    public void testStatic() {
        printStream(ArmLivermoreELFN10.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRaw() {
        rawDump(ArmLivermoreELFN10.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRawFromTxtDump() {
        rawDump(ArmLivermoreELFN10.innerprod.asTxtDump().toStaticStream());
    }

    @Test
    public void testTrace() {
        printStream(ArmLivermoreELFN10.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRaw() {
        rawDump(ArmLivermoreELFN10.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRawFromTxtDump() {
        rawDump(ArmLivermoreELFN10.innerprod.asTraceTxtDump().toTraceStream());
    }
}
