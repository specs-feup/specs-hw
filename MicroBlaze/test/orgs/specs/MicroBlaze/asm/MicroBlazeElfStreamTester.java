package orgs.specs.MicroBlaze.asm;

import java.io.File;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeElfStreamTester {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/test.elf");
        fd.deleteOnExit();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                System.out.println(inst);
            }
        }

    }
}
