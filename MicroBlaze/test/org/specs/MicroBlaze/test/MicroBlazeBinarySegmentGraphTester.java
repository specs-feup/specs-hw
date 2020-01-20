package org.specs.MicroBlaze.test;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeBinarySegmentGraphTester {

    public List<BinarySegment> getFrequentStatiSegments() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/helloworld/helloworld.elf");
        fd.deleteOnExit();

        try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            var bbd = new FrequentStaticSequenceDetector(el);
            var segments = bbd.detectSegments();
            return segments;
        }
    }

    @Test
    public void test() {
        var segs = getFrequentStatiSegments();

        var graph0 = new BinarySegmentGraph(segs.get(0));
        graph0.printDotty();

        var graph1 = new BinarySegmentGraph(segs.get(1));
        graph1.printDotty();
    }
}
