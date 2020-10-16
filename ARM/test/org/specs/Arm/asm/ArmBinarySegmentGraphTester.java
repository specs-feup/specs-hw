package org.specs.Arm.asm;

import java.io.File;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsIo;

public class ArmBinarySegmentGraphTester {

    private File openFile() {
        // File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/matmul.elf");
        // File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/cholesky.elf");

        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/cholesky.txt");
        // File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/cholesky_trace.txt");
        fd.deleteOnExit();
        return fd;
    }

    private BinarySegmentGraph convertSegmentToGraph(BinarySegment seg) {
        return BinarySegmentGraph.newInstance(seg);
    }

    private void convertBundleToGraph(SegmentBundle bund) {
        int safetycounter = 0; // to prevent lots of printing (just for testing purposes)
        for (BinarySegment seg : bund.getSegments()) {
            var graph0 = convertSegmentToGraph(seg);
            if (safetycounter < 50) {
                if (graph0.getCpl() >= 2 && graph0.getSegment().getContexts().size() >= 1) {
                    graph0.generateOutput();
                    safetycounter++;
                }
            }
        }
    }

    private void getSegments(InstructionStream el, SegmentDetector bbd) {
        var bundle = bbd.detectSegments(el);
        var graphbundle = GraphBundle.newInstance(bundle);
        graphbundle.generateOutput(data -> data.getSegment().getContexts().size() > 10);
        // System.out.println(graphbundle.getAverageIPC());
    }

    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void testStaticFrequentSequence() {

        try (ArmElfStream el = new ArmElfStream(openFile())) {
            var bbd = new FrequentStaticSequenceDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testStaticBasicBlock() {

        try (ArmElfStream el = new ArmElfStream(openFile())) {
            var bbd = new StaticBasicBlockDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testTraceFrequenceSequence() {
        try (ArmTraceStream el = new ArmTraceStream(openFile())) {
            var bbd = new FrequentTraceSequenceDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testTraceBasicBlock() {
        try (ArmTraceStream el = new ArmTraceStream(openFile())) {
            var bbd = new TraceBasicBlockDetector();
            getSegments(el, bbd);
        }
    }
}
