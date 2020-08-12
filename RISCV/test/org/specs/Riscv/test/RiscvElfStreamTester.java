package org.specs.Riscv.test;

import org.junit.Test;
import org.specs.Riscv.stream.RiscvElfStream;

import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTestUtils;

public class RiscvElfStreamTester {

    @Test
    public void testStatic() {
        InstructionStreamTestUtils.printStream("org/specs/Riscv/asm/test64.elf", RiscvElfStream.class);
    }

    @Test
    public void testStaticRaw() {
        InstructionStreamTestUtils.rawDump("org/specs/Riscv/asm/test64.elf", RiscvElfStream.class);
    }
}
