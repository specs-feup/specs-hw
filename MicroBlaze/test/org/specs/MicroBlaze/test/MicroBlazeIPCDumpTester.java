package org.specs.MicroBlaze.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeIPCDumpTester {

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
        filenames.add("org/specs/MicroBlaze/asm/cholesky.elf");
        /* filenames.add("org/specs/MicroBlaze/asm/diffpredict.elf");
        filenames.add("org/specs/MicroBlaze/asm/glinearrec.elf");
        filenames.add("org/specs/MicroBlaze/asm/hydro.elf");
        filenames.add("org/specs/MicroBlaze/asm/hydro2d.elf");
        filenames.add("org/specs/MicroBlaze/asm/innerprod.elf");
        filenames.add("org/specs/MicroBlaze/asm/matmul.elf");*/

        for (String file : filenames) {

            File fd = openFile(file);

            // try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
            // var bbd = new FrequentStaticSequenceDetector(el);
            // var bbd = new StaticBasicBlockDetector(el);

            try (MicroBlazeTraceStream el = new MicroBlazeTraceStream(fd)) {

                // var bbd = new FrequentTraceSequenceDetector(el);
                var bbd = new TraceBasicBlockDetector();
                var bundle = bbd.detectSegments(el);
                System.out.println(file);
                dumpIPC(bundle);
            }
        }
    }
}
