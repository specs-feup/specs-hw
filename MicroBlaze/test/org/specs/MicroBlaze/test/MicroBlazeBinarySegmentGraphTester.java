package org.specs.MicroBlaze.test;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
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

    private void getSegments(InstructionStream el, SegmentDetector bbd) {
        var bundle = bbd.detectSegments(el);
        var gbundle = GraphBundle.newInstance(bundle);

        gbundle.generateOutput(data -> data.getCpl() == 3);

        /*
        var list = gbundle
                .getGraphs(data -> data.getCpl() == 3);
        for (BinarySegmentGraph graph : list) {
            graph.generateOutput();
        }*/
    }

    @Test
    public void testStaticFrequentSequence() {
        try (var el = new MicroBlazeElfStream(openFile())) {
            var bbd = new FrequentStaticSequenceDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testStaticBasicBlock() {
        try (var el = new MicroBlazeElfStream(openFile())) {
            var bbd = new StaticBasicBlockDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testTraceFrequenceSequence() {
        try (var el = new MicroBlazeTraceStream(openFile())) {
            var bbd = new FrequentTraceSequenceDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testTraceBasicBlock() {
        try (var el = new MicroBlazeTraceStream(openFile())) {
            var bbd = new TraceBasicBlockDetector();
            getSegments(el, bbd);
        }
    }
}
