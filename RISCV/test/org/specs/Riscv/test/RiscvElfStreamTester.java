package org.specs.Riscv.test;

import java.io.File;

import org.junit.Test;
import org.specs.Riscv.stream.RiscvElfStream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsIo;

public class RiscvElfStreamTester {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/Riscv/asm/test64.elf");
        fd.deleteOnExit();

        try (RiscvElfStream el = new RiscvElfStream(fd)) {
            // el.rawDump();
            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                inst.printInstruction();
            }
        }
    }
}
