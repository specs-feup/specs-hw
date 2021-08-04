package org.specs.Arm.asm.stream;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreELFN10;
import org.specs.Arm.provider.ArmObjDumpProvider;
import org.specs.Arm.provider.ArmTraceDumpProvider;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTestUtils;

public class ArmStreamTester {

    @Test
    public void testStatic() {
        InstructionStreamTestUtils.printStream(
                new ArmElfStream(ArmLivermoreELFN10.innerprod));
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTestUtils.rawDump(
                new ArmElfStream(ArmLivermoreELFN10.innerprod));
    }

    @Test
    public void testStaticRawFromTxtDump() {
        InstructionStreamTestUtils.rawDump(
                new ArmElfStream(new ArmObjDumpProvider(ArmLivermoreELFN10.innerprod)));
    }

    @Test
    public void testTrace() {
        InstructionStreamTestUtils.printStream(
                new ArmTraceStream(ArmLivermoreELFN10.innerprod));
    }

    @Test
    public void testTraceRaw() {
        InstructionStreamTestUtils.rawDump(
                new ArmTraceStream(ArmLivermoreELFN10.innerprod));
    }

    @Test
    public void testTraceRawFromTxtDump() {
        InstructionStreamTestUtils.rawDump(
                new ArmTraceStream(new ArmTraceDumpProvider(ArmLivermoreELFN10.innerprod)));
    }
}
