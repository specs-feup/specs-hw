package org.specs.Arm.asm;

import java.io.File;

import org.junit.Test;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.util.SpecsIo;

public class ArmTraceStreamTester {

    @Test
    public void test() {

        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/matmul.elf");
        fd.deleteOnExit();

        // ArmTraceStream el = new ArmTraceStream(fd);
        try (ArmTraceStream el = new ArmTraceStream(fd)) {

            el.rawDump();

            /*Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                inst.printInstruction();
            }*/
        }
    }
}
