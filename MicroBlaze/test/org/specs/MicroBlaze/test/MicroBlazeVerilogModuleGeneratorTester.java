package org.specs.MicroBlaze.test;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.hardwaregeneration.VerilogModuleGenerator;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeVerilogModuleGeneratorTester {

    private List<BinarySegment> getSequences() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/test.elf");
        fd.deleteOnExit();

        List<BinarySegment> bblist = null;
        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            var bbd = new FrequentStaticSequenceDetector(el);
            var bundle = bbd.detectSegments();
            bblist = bundle.getSegments();

        }
        return bblist;
    }

    @Test
    public void test() {
        List<BinarySegment> bblist = getSequences();
        VerilogModuleGenerator gen1 = new VerilogModuleGenerator();
        gen1.generateHardware(bblist.get(0));
        return;
    }

}
