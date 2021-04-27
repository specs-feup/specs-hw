package org.specs.MicroBlaze.test.tracer;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.tracer.StaticGraphGenerator;
import pt.up.fe.specs.binarytranslation.tracer.StreamTracer;
import pt.up.fe.specs.binarytranslation.tracer.TraceBasicBlock;
import pt.up.fe.specs.binarytranslation.tracer.TraceSuperBlock;
import pt.up.fe.specs.binarytranslation.tracer.TraceUnit;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeTracerTester {

    /*
     * test graphing a trace
     */
    @Test
    public void testTraceGraphingStatic() {

        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        try (var istream = new MicroBlazeElfStream(fd)) {

            // using TreeNode
            // var graph = StaticGraphGenerator.generateStaticGraph(istream);
            // System.out.println(DottyGenerator.buildDotty(graph.getHead()));

            // Using JGraphT
            var graph = StaticGraphGenerator.generateStaticGraph(istream);

            var exporter = new DOTExporter<TraceUnit, DefaultEdge>();
            exporter.setVertexAttributeProvider(v -> {
                Map<String, Attribute> map = new LinkedHashMap<>();
                map.put("label", DefaultAttribute.createAttribute(v.getStart().getAddress().toString()));
                return map;
            });
            var writer = new StringWriter();
            exporter.exportGraph(graph, writer);
            System.out.println(writer.toString());
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
