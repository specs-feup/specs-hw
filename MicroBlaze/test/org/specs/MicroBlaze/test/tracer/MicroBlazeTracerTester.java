package org.specs.MicroBlaze.test.tracer;

import java.util.HashMap;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.graph2dotty.DottyGenerator;
import pt.up.fe.specs.binarytranslation.tracer.StreamTracer;
import pt.up.fe.specs.binarytranslation.tracer.TraceBasicBlock;
import pt.up.fe.specs.binarytranslation.tracer.TraceGraphNode;
import pt.up.fe.specs.binarytranslation.tracer.TraceSuperBlock;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeTracerTester {

    /*
     * test graphing a trace
     */
    @Test
    public void testTraceGraphingStatic() {

        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        try (var istream = new MicroBlazeElfStream(fd)) {

            // head
            TraceGraphNode head = null;

            // basic blocks
            var tracer = new StreamTracer(istream);
            int i = 20; // just for testing!
            while (tracer.hasNext()) {

                // next block
                var next = tracer.nextBasicBlock();

                // first
                if (head == null)
                    head = new TraceGraphNode(next);

                // others
                else {
                    var newBlock = new TraceGraphNode(next);
                    // if (next.follows(head.getUnit())) {
                    head.addChild(newBlock);
                    head = newBlock;
                    // }
                }
                i--;
                if (i == 0)
                    break;
            }

            // recover head
            while (head.hasParent())
                head = head.getParent();

            var dottyprinter = new DottyGenerator<TraceGraphNode>();
            dottyprinter.generateDotty(head);

            istream.close();
        }
    }

    /*
     * Get BasicBlockTraceUnit's from elf stream (does this even make sense???) 
     */
    @Test
    public void testBasicBlockTraceUnit_static() {

        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        try (var istream = new MicroBlazeElfStream(fd)) {

            // basic blocks
            var tracer = new StreamTracer(istream);
            while (tracer.hasNext()) {
                System.out.println(tracer.nextBasicBlock().toString());
            }

            istream.close();
        }
    }

    /*
     * Get BasicBlockTraceUnit's from trace stream 
     */
    @Test
    public void testBasicBlockTraceUnit() {

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
    public void testSuperBlockTraceUnit() {

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
