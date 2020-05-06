package org.specs.Arm.asm;

import java.io.File;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.SegmentDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
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

    private void getSegments(SegmentDetector bbd) {

        var bundle = bbd.detectSegments();

        int safetycounter = 0; // to prevent lots of printing (just for testing purposes)
        for (BinarySegment seg : bundle.getSegments()) {
            var graph0 = BinarySegmentGraph.newInstance(seg);
            if (safetycounter < 50) {
                if (graph0.getCpl() >= 2 && graph0.getSegment().getContexts().size() >= 1) {

                    graph0.generateOutput();
                    safetycounter++;
                }
            }
        }
    }

    @Test
    public void testStaticFrequentSequence() {

        try (ArmElfStream el = new ArmElfStream(openFile())) {
            var bbd = new FrequentStaticSequenceDetector(el);
            getSegments(bbd);
        }
    }

    @Test
    public void testStaticBasicBlock() {

        try (ArmElfStream el = new ArmElfStream(openFile())) {
            var bbd = new StaticBasicBlockDetector(el);
            getSegments(bbd);
        }
    }

    @Test
    public void testTraceFrequenceSequence() {
        try (ArmTraceStream el = new ArmTraceStream(openFile())) {
            var bbd = new FrequentTraceSequenceDetector(el);
            getSegments(bbd);
        }
    }

    @Test
    public void testTraceBasicBlock() {
        try (ArmTraceStream el = new ArmTraceStream(openFile())) {
            var bbd = new TraceBasicBlockDetector(el);
            getSegments(bbd);
        }
    }
}
