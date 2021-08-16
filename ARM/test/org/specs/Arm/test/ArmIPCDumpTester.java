package org.specs.Arm.test;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN10;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;

public class ArmIPCDumpTester {

    /*
     * Dump out the IPC for all detected segments
     */
    private void dumpIPC(SegmentBundle bundle) {
        for (BinarySegment seg : bundle.getSegments()) {
            var graph0 = BinarySegmentGraph.newInstance(seg);
            /* System.out.println(seg.getContexts().size() + "\t" + graph0.getEstimatedIPC() + "\t"
            + seg.getSegmentLength() + "\t" + graph0.getCpl());*/

            graph0.generateOutput();
        }
    }

    @Test
    public void dumpIPCAllFiles() {

        for (var elf : ArmLivermoreN10.values()) {
            try (var el = new ArmTraceStream(elf.toApplication())) {

                // var bbd = new FrequentTraceSequenceDetector(el);
                var bbd = new TraceBasicBlockDetector();
                var bundle = bbd.detectSegments(el);
                System.out.println(elf.getFilename());
                dumpIPC(bundle);
            }
        }
    }
}
