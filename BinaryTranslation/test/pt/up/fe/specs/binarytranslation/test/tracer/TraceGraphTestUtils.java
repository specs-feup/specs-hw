package pt.up.fe.specs.binarytranslation.test.tracer;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;
import pt.up.fe.specs.binarytranslation.tracer.StreamBasicBlock;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnitGenerator;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnitGraphGenerator;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class TraceGraphTestUtils {

    /*
     * test graphing a trace
     */
    public static void testTraceStreamGraph(ELFProvider elfprovider,
            Class<? extends TraceInstructionStream> streamClass) {

        try (var istream = elfprovider.toTraceStream()) {

            // Using JGraphT
            var app = istream.getApp();
            var graphGenerator = new StreamUnitGraphGenerator(istream);
            var graph = graphGenerator.generateInstructionGraph(app.getKernelStart(), app.getKernelStop());
            var pngName = "./output/" + app.getAppName().replace(".elf", "_inst.png");
            BinaryTranslationUtils.renderDotty(pngName, graph.toDotty());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * Get BasicBlockTraceUnit's from trace stream and print
     */
    @Test
    public static void testBasicBlockTraceUnit(ELFProvider elfprovider,
            Class<? extends TraceInstructionStream> streamClass) {

        int i = 5;
        try (var istream = elfprovider.toTraceStream()) {

            // basic blocks
            var tracer = new StreamUnitGenerator(istream);
            StreamBasicBlock tbb = null;
            while ((tbb = tracer.nextBasicBlock()) != null && i > 0) {
                System.out.println(tbb.toString());
                i--;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * 
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
     */
}
