package orgs.specs.MicroBlaze.asm;

import java.io.File;
import java.util.List;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.loopdetector.StaticBasicBlockDetector;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeStaticBasicBlockDetectorTest {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/test.elf");
        fd.deleteOnExit();

        MicroBlazeElfStream el = new MicroBlazeElfStream(fd);
        var bbd = new StaticBasicBlockDetector(el);
        List<BinarySegment> bblist = bbd.detectLoops();
    }

}
