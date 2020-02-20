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
import pt.up.fe.specs.binarytranslation.binarysegments.detection.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeBinarySegmentGraphTester {

    private File openFile() {
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/matmul/matmul.elf");
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/innerprod/innerprod.elf");
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/cholesky/cholesky.elf");
        fd.deleteOnExit();
        return fd;
    }

    private void getSegments(SegmentDetector bbd) {
        var segments = bbd.detectSegments();

        int safetycounter = 0; // to prevent lots of printing (just for testing purposes)
        for (BinarySegment seg : segments) {
            var graph0 = BinarySegmentGraph.newInstance(seg);
            if (safetycounter < 50) {
                if (graph0.getCpl() >= 1 && graph0.getSegment().getContexts().size() >= 1) {
                    // graph0.getSegment().printSegment();
                    // System.out.println(graph0.getInitiationInterval());
                    // System.out.println(graph0.getEstimatedIPC());
                    // graph0.printDotty();
                    // graph0.printDotty("graph_" + Integer.toString(seg.hashCode()) + ".dot");

                    graph0.generateOutput();
                    safetycounter++;
                }
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

    @Test
    public void testTraceBasicBlock() {
        try (MicroBlazeTraceStream el = new MicroBlazeTraceStream(openFile())) {
            var bbd = new TraceBasicBlockDetector(el);
            getSegments(bbd);
        }
    }
}
