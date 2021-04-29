package org.specs.MicroBlaze.test.tracer;

import java.util.HashMap;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.tracer.StreamBasicBlock;
import pt.up.fe.specs.binarytranslation.tracer.StreamSuperBlock;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnitGenerator;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnitGraphGenerator;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeTracerTester {

    @Test
    public void testTraceGraphingStaticAllELFs() {
        for (var file : MicroBlazeLivermoreELFN10.values()) {
            testTraceGraphingStatic(file);
        }
        // testTraceGraphingStatic(MicroBlazeLivermoreELFN10.innerprod);
    }

    /*
     * test graphing a trace
     */
    private void testTraceGraphingStatic(ELFProvider elf) {

        var fd = BinaryTranslationUtils.getFile(elf);
        try (var istream = new MicroBlazeTraceStream(fd)) {

            // Using JGraphT
            var graphGenerator = new StreamUnitGraphGenerator(istream);
            var graph = graphGenerator.generateBasicBlockGraph(elf.getKernelStart(), elf.getKernelStop());
            var pngName = "./output/" + elf.getFilename().replace(".elf", "_basic.png");
            BinaryTranslationUtils.renderDotty(pngName, graph.toDotty());
        }
    }

    /*
     * Get BasicBlockTraceUnit's from elf stream (does this even make sense???) 
     
     // TODO: its not easy to generate a static elf graph just like this,
     // since consecutive basic blocks in the elf dump, when read in sequence,
     // dont necessarily flow into eachother... e.g., ends of functions
     // dont flow into other functions
    
     // in the best case, I can generate static function graphs <---- TODO?? 
     
    @Test
    public void testBasicBlockTraceUnit_static() {
    
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        try (var istream = new MicroBlazeElfStream(fd)) {
    
            // basic blocks
            var tracer = new StreamUnitGenerator(istream);
            while (tracer.hasNext()) {
                System.out.println(tracer.nextBasicBlock().toString());
            }
    
            istream.close();
        }
    }
    */

    /*
     * Get BasicBlockTraceUnit's from trace stream 
     */
    @Test
    public void testBasicBlockTraceUnit() {

        int i = 5;
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        try (var istream = new MicroBlazeTraceStream(fd)) {

            // basic blocks
            var tracer = new StreamUnitGenerator(istream);
            StreamBasicBlock tbb = null;
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
        var superblockMap = new HashMap<StreamSuperBlock, Integer>();

        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.matmul);
        try (var istream = new MicroBlazeTraceStream(fd)) {

            // super block
            var tracer = new StreamUnitGenerator(istream);
            while (!istream.isClosed()) {
                var sblock = tracer.nextSuperBlock();
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
