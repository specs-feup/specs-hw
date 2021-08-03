package org.specs.Riscv.test;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvELFProvider;
import org.specs.Riscv.provider.RiscvLivermoreELFN100iam;
import org.specs.Riscv.stream.RiscvElfStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class RiscvBinarySegmentGraphTester {

    private RiscvELFProvider openFile() {
        return RiscvLivermoreELFN100iam.cholesky100;
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
