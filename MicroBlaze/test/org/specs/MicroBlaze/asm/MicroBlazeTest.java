package org.specs.MicroBlaze.asm;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.asmparser.MicroBlazeInstructionParsers;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.asmparser.IsaParser;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeTest {

    @Test
    public void testArmParser() {
        IsaParser parser = MicroBlazeInstructionParsers.getMicroBlazeIsaParser();

        StringBuilder output = new StringBuilder();

        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/test.elf");
        fd.deleteOnExit();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                var data = parser.parse(inst);
                output.append(data).append("\n");
            }
        }

        System.out.print(output);
    }
}
