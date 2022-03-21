/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.binarytranslation.test.tracer;

import java.util.function.BiFunction;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnitGraph;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnitGraphGenerator;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class TraceGraphTestUtils {

    private static StreamUnitGraph generateInstructionGraph(StreamUnitGraphGenerator gen, Application app) {
        return gen.generateInstructionGraph(app.getKernelStart(), app.getKernelStop());
    }

    private static StreamUnitGraph generateBasicBlockGraph(StreamUnitGraphGenerator gen, Application app) {
        return gen.generateBasicBlockGraph(app.getKernelStart(), app.getKernelStop());
    }

    /*
     * test graphing a trace
     */
    private static void _testTraceStreamGraph(
            TraceInstructionStream istream, BiFunction<StreamUnitGraphGenerator, Application, StreamUnitGraph> hook) {

        // Using JGraphT
        var app = istream.getApp();
        var graphGenerator = new StreamUnitGraphGenerator(istream);
        var graph = hook.apply(graphGenerator, app);
        var pngName = "./output/" + app.getAppName().replace(".elf", "_inst.png");
        BinaryTranslationUtils.renderDotty(pngName, graph.toDotty());
    }

    /*
     * test graphing a trace
     */
    public static void testTraceStreamGraph(
            TraceInstructionStream istream) {
        _testTraceStreamGraph(istream, TraceGraphTestUtils::generateInstructionGraph);

        /*
        // Using JGraphT
        var app = istream.getApp();
        var graphGenerator = new StreamUnitGraphGenerator(istream);
        var graph = graphGenerator.generateInstructionGraph(app.getKernelStart(), app.getKernelStop());
        var pngName = "./output/" + app.getAppName().replace(".elf", "_inst.png");
        BinaryTranslationUtils.renderDotty(pngName, graph.toDotty());*/
    }

    /*
     * Get BasicBlockTraceUnit's from trace stream and print
     */
    @Test
    public static void testBasicBlockTraceUnit(
            TraceInstructionStream istream) {
        _testTraceStreamGraph(istream, TraceGraphTestUtils::generateBasicBlockGraph);

        // Using JGraphT
        /*var app = istream.getApp();
        var graphGenerator = new StreamUnitGraphGenerator(istream);
        var graph = graphGenerator.generateBasicBlockGraph(app.getKernelStart(), app.getKernelStop());
        var pngName = "./output/" + app.getAppName().replace(".elf", "_inst.png");
        BinaryTranslationUtils.renderDotty(pngName, graph.toDotty());*/

        /*
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
        }*/
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
