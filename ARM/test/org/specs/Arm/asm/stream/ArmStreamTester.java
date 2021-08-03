package org.specs.Arm.asm.stream;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreELFN10;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTestUtils;

public class ArmStreamTester {

    @Test
    public void testStatic() {
        InstructionStreamTestUtils.printStream(
                ArmLivermoreELFN10.innerprod, ArmElfStream.class);
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTestUtils.rawDump(
                ArmLivermoreELFN10.innerprod, ArmElfStream.class);
    }

    @Test
    public void testTrace() {
        InstructionStreamTestUtils.printStream(
                ArmLivermoreELFN10.innerprod, ArmTraceStream.class);
    }

    @Test
    public void testTraceRaw() {
        InstructionStreamTestUtils.rawDump(
                ArmLivermoreELFN10.innerprod, ArmTraceStream.class);
    }
}
