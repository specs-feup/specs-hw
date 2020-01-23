package org.specs.Arm.asm;

import java.io.File;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsIo;

public class ArmElfStreamTester {

    @Test
    public void test() {

        // File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/helloworld64_from_vivado_flow/helloworld64.elf");
        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/test.txt");
        fd.deleteOnExit();

        try (ArmElfStream el = new ArmElfStream(fd);) {

            // el.rawDump();

            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                inst.printInstruction();
            }
        }
    }
}
