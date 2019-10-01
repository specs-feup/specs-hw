package org.specs.MicroBlaze.asm;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.asmparser.MicroBlazeInstructionParsers;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.asmparser.IsaParser;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;

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

        String expected = SpecsStrings
                .normalizeFileContents(SpecsIo.getResource("org/specs/MicroBlaze/asm/test/arm_parser.result"), true);
        String current = SpecsStrings.normalizeFileContents(output.toString(), true);

        assertEquals(expected, current);
    }

}
