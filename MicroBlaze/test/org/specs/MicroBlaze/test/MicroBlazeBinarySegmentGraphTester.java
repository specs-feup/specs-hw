package org.specs.MicroBlaze.test;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.*;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.*;
import pt.up.fe.specs.binarytranslation.graphs.*;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeBinarySegmentGraphTester {

    private File openFile() {

        // static
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/matmul.txt");
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/innerprod.txt");
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/cholesky.txt");
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/innerprod.txt");

        // dynamic
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/cholesky_trace.txt");

        fd.deleteOnExit();
        return fd;
    }

    private void getSegments(SegmentDetector bbd) {
        var bundle = bbd.detectSegments();
        var gbundle = GraphBundle.newInstance(bundle);

        var list = gbundle.getGraphs(data -> data.getCpl() >= 3);
        for (BinarySegmentGraph graph : list) {
            graph.generateOutput();
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
