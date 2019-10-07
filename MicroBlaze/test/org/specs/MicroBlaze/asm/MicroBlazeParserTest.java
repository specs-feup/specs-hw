package org.specs.MicroBlaze.asm;

import java.io.File;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeParserTest {

    // @Test
    public void testMicroBlazeParser() {

        StringBuilder output = new StringBuilder();

        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/test.elf");
        fd.deleteOnExit();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                output.append(inst.getFields()).append("\n");
            }
        }

        System.out.print(output);
    }
}
