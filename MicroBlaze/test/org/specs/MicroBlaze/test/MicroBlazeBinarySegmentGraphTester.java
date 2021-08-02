package org.specs.MicroBlaze.test;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeELFProvider;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class MicroBlazeBinarySegmentGraphTester {

    private MicroBlazeELFProvider getELF() {
        return MicroBlazeLivermoreELFN10.cholesky;
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
        try (var el = new MicroBlazeElfStream(getELF())) {
            var bbd = new FrequentStaticSequenceDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testStaticBasicBlock() {
        try (var el = new MicroBlazeElfStream(getELF())) {
            var bbd = new StaticBasicBlockDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testTraceFrequenceSequence() {
        try (var el = new MicroBlazeTraceStream(getELF())) {
            var bbd = new FrequentTraceSequenceDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testTraceBasicBlock() {
        try (var el = new MicroBlazeTraceStream(getELF())) {
            var bbd = new TraceBasicBlockDetector();
            getSegments(el, bbd);
        }
    }
}
