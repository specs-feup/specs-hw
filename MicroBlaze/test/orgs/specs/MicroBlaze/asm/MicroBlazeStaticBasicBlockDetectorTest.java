package orgs.specs.MicroBlaze.asm;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.asm.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.loopdetector.StaticBasicBlockDetector;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeStaticBasicBlockDetectorTest {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/test.elf");
        fd.deleteOnExit();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            var bbd = new StaticBasicBlockDetector(el);
            List<BinarySegment> bblist = bbd.detectLoops();

            for (BinarySegment bs : bblist) {
                System.out.print("Basic Block: \n");
                for (Instruction is : bs.getInstructions()) {
                    is.printInstruction();
                }
                System.out.print("\n\n");
            }
        }
        return;
    }
}
