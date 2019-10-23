package org.specs.MicroBlaze.asm;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeParserTester {

    @Test
    public void testMicroBlazeParser() {

        StringBuilder output = new StringBuilder();

        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/test.elf");
        fd.deleteOnExit();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                output.append(inst.getFieldData()).append("\n");
            }
        }

        System.out.print(output);
    }
}
