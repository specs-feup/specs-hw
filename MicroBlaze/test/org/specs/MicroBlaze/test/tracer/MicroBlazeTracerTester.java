package org.specs.MicroBlaze.test.tracer;

import java.util.HashMap;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.tracer.StreamTracer;
import pt.up.fe.specs.binarytranslation.tracer.TraceBasicBlock;
import pt.up.fe.specs.binarytranslation.tracer.TraceSuperBlock;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeTracerTester {

    @Test
    public void testBasicBlockTrace() {

        int i = 5;
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        try (var istream = new MicroBlazeTraceStream(fd)) {

            // basic blocks
            var tracer = new StreamTracer(istream);
            TraceBasicBlock tbb = null;
            while ((tbb = tracer.nextBasicBlock()) != null && i > 0) {
                System.out.println(tbb.toString());
                i--;
            }

            istream.close();
        }
    }

    @Test
    public void testSuperBlockTrace() {

        // <superblock, hit counter>
        var superblockMap = new HashMap<TraceSuperBlock, Integer>();

        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.matmul);
        try (var istream = new MicroBlazeTraceStream(fd)) {

            // super block? max size 50
            var tracer = new StreamTracer(istream);
            while (!istream.isClosed()) {
                var sblock = tracer.nextSuperBlock(50);
                if (sblock == null)
                    continue;

                if (superblockMap.containsKey(sblock)) {
                    var ctr = superblockMap.get(sblock);
                    superblockMap.put(sblock, ++ctr);
                } else {
                    superblockMap.put(sblock, 1);
                }

                // if (sblock != null)
                // System.out.println(sblock.toString());
            }
        }

        var itr = superblockMap.entrySet().iterator();
        while (itr.hasNext()) {
            var entry = itr.next();
            System.out.println("Iterations " + entry.getValue()
                    + " - hash: " + entry.getKey().hashCode() + "\n" + entry.getKey());
        }
    }

}
