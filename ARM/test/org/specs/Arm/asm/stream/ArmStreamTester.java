package org.specs.Arm.asm.stream;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTestUtils;

public class ArmStreamTester {

    @Test
    public void testStatic() {
        InstructionStreamTestUtils.printStream("org/specs/Arm/asm/cholesky.txt", ArmElfStream.class);
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTestUtils.rawDump("org/specs/Arm/asm/cholesky.txt", ArmElfStream.class);
    }

    @Test
    public void testTrace() {
        InstructionStreamTestUtils.printStream("org/specs/Arm/asm/cholesky.elf", ArmTraceStream.class);
    }

    @Test
    public void testTraceRaw() {
        InstructionStreamTestUtils.rawDump("org/specs/Arm/asm/cholesky.elf", ArmTraceStream.class);
    }
}
