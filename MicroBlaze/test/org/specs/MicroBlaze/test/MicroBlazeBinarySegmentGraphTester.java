package org.specs.MicroBlaze.test;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.SegmentDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeBinarySegmentGraphTester {

    private File openFile() {
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/helloworld/helloworld.elf");
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/test/matmul/matmul_n4096_l1000.elf");
        fd.deleteOnExit();
        return fd;
    }

    private void getSegments(SegmentDetector bbd) {
        var segments = bbd.detectSegments();

        for (BinarySegment seg : segments) {
            var graph0 = BinarySegmentGraph.newInstance(seg);
            if (graph0.getCpl() >= 0 && graph0.getSegment().getContexts().size() > 1) {
                // graph0.getSegment().printSegment();
                // System.out.println(graph0.getInitiationInterval());
                // System.out.println(graph0.getEstimatedIPC());
                // graph0.printDotty();
                // graph0.printDotty("graph_" + Integer.toString(seg.hashCode()) + ".dot");

                graph0.generateOutput();
            }
        }
    }

    @Test
    public void testStaticFrequentSequence() {
        try (MicroBlazeElfStream el = new MicroBlazeElfStream(openFile())) {
            var bbd = new FrequentStaticSequenceDetector(el);
            getSegments(bbd);
        }
    }

    @Test
    public void testStaticBasicBlock() {
        try (MicroBlazeElfStream el = new MicroBlazeElfStream(openFile())) {
            var bbd = new StaticBasicBlockDetector(el);
            getSegments(bbd);
        }
    }

    @Test
    public void testTraceFrequenceSequence() {
        try (MicroBlazeTraceStream el = new MicroBlazeTraceStream(openFile())) {
            var bbd = new FrequentTraceSequenceDetector(el);
            getSegments(bbd);
        }
    }
}
