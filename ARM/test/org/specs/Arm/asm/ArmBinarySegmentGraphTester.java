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
import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
import pt.up.fe.specs.util.SpecsIo;

public class ArmBinarySegmentGraphTester {

    private File openFile() {
        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/aarch64_bare_metal_qemu/test64.elf");
        fd.deleteOnExit();
        return fd;
    }

    private void getSegments(SegmentDetector bbd) {
        var segments = bbd.detectSegments();

        for (BinarySegment seg : segments) {
            var graph0 = BinarySegmentGraph.newInstance(seg);
            graph0.printDotty();
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
}
