package org.specs.Arm.asm;

import java.io.File;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsIo;

public class ArmElfStreamTester {

    @Test
    public void test() {

        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/aarch64_bare_metal_qemu/test64.elf");
        fd.deleteOnExit();

        try (ArmElfStream el = new ArmElfStream(fd);) {
            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                inst.printInstruction();
            }
        }

    }
}
