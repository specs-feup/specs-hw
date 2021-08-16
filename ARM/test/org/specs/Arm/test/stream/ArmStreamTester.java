package org.specs.Arm.test.stream;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN10;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTester;

public class ArmStreamTester extends InstructionStreamTester {

    @Test
    public void testStatic() {
        printStream(ArmLivermoreN10.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRaw() {
        rawDump(ArmLivermoreN10.innerprod.toStaticStream());
    }

    @Test
    public void testStaticRawFromTxtDump() {
        rawDump(ArmLivermoreN10.innerprod.asTxtDump().toStaticStream());
    }

    @Test
    public void testTrace() {
        printStream(ArmLivermoreN10.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRaw() {
        rawDump(ArmLivermoreN10.innerprod.toTraceStream());
    }

    @Test
    public void testTraceRawFromTxtDump() {
        rawDump(ArmLivermoreN10.innerprod.asTraceTxtDump().toTraceStream());
    }
}
