package org.specs.Arm.asm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
import pt.up.fe.specs.util.SpecsIo;

public class ArmIPCDumpTester {

    private File openFile(String filename) {
        File fd = SpecsIo.resourceCopy(filename);
        fd.deleteOnExit();
        return fd;
    }

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

        List<String> filenames = new ArrayList<String>();
        filenames.add("org/specs/Arm/asm/cholesky.elf");
        /*filenames.add("org/specs/Arm/asm/diffpredict.elf");
        filenames.add("org/specs/Arm/asm/glinearrec.elf");
        filenames.add("org/specs/Arm/asm/hydro.elf");
        filenames.add("org/specs/Arm/asm/hydro2d.elf");
        filenames.add("org/specs/Arm/asm/innerprod.elf");
        filenames.add("org/specs/Arm/asm/matmul.elf");*/

        for (String file : filenames) {

            File fd = openFile(file);

            // try (ArmElfStream el = new ArmElfStream(fd)) {
            // var bbd = new FrequentStaticSequenceDetector(el);
            // var bbd = new StaticBasicBlockDetector(el);

            try (ArmTraceStream el = new ArmTraceStream(fd)) {

                // var bbd = new FrequentTraceSequenceDetector(el);
                var bbd = new TraceBasicBlockDetector(el);
                var bundle = bbd.detectSegments();
                System.out.println(file);
                dumpIPC(bundle);
            }
        }
    }
}
