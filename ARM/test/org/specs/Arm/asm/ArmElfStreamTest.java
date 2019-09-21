package org.specs.Arm.asm;

import java.io.File;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.util.SpecsIo;

public class ArmElfStreamTest {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/test/test.elf");
        fd.deleteOnExit();

        ArmElfStream el = new ArmElfStream(fd);

        Instruction inst = null;
        while ((inst = el.nextInstruction()) != null) {
            System.out.println(inst);
        }
    }
}
