package org.specs.Riscv.test;

import java.io.File;

import org.junit.Test;
import org.specs.Riscv.stream.RiscvElfStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsIo;

public class RiscvBinarySegmentGraphTester {

    private File openFile() {

        // static
        File fd = SpecsIo.resourceCopy("org/specs/Riscv/asm/test64.elf");
        fd.deleteOnExit();
        return fd;
    }

    private void getSegments(InstructionStream el, SegmentDetector bbd) {
        var bundle = bbd.detectSegments(el);
        var gbundle = GraphBundle.newInstance(bundle);

        var list = gbundle.getGraphs(data -> data.getCpl() >= 1);
        for (BinarySegmentGraph graph : list) {
            graph.generateOutput();
        }
    }

    @Test
    public void testStaticFrequentSequence() {
        try (RiscvElfStream el = new RiscvElfStream(openFile())) {
            var bbd = new FrequentStaticSequenceDetector();
            getSegments(el, bbd);
        }
    }

    @Test
    public void testStaticBasicBlock() {
        try (RiscvElfStream el = new RiscvElfStream(openFile())) {
            var bbd = new StaticBasicBlockDetector();
            getSegments(el, bbd);
        }
    }
}
