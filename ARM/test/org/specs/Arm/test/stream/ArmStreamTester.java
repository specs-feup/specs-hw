package org.specs.Arm.test.stream;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreELFN10;
import org.specs.Arm.provider.ArmObjDumpProvider;
import org.specs.Arm.provider.ArmTraceDumpProvider;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTester;

public class ArmStreamTester {

    @Test
    public void testStatic() {
        InstructionStreamTester.printStream(
                new ArmElfStream(ArmLivermoreELFN10.innerprod));
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTester.rawDump(
                new ArmElfStream(ArmLivermoreELFN10.innerprod));
    }

    @Test
    public void testStaticRawFromTxtDump() {
        InstructionStreamTester.rawDump(
                new ArmElfStream(new ArmObjDumpProvider(ArmLivermoreELFN10.innerprod)));
    }

    @Test
    public void testTrace() {
        InstructionStreamTester.printStream(
                new ArmTraceStream(ArmLivermoreELFN10.innerprod));
    }

    @Test
    public void testTraceRaw() {
        InstructionStreamTester.rawDump(
                new ArmTraceStream(ArmLivermoreELFN10.innerprod));
    }

    @Test
    public void testTraceRawFromTxtDump() {
        InstructionStreamTester.rawDump(
                new ArmTraceStream(new ArmTraceDumpProvider(ArmLivermoreELFN10.innerprod)));
    }
}
