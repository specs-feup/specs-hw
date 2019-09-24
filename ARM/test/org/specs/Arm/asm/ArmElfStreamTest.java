package org.specs.Arm.asm;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;

public class ArmElfStreamTest {

    @Test
    public void test() {

        SpecsSystem.runProcess(Arrays.asList("dir"), true, true);

        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/test/test.elf");
        fd.deleteOnExit();

        try (ArmElfStream el = new ArmElfStream(fd);) {
            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                System.out.println(inst);
            }
        }

    }
}
