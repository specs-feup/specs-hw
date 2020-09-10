/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.binarytranslation.graphs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import pt.up.fe.specs.binarytranslation.BinaryTranslationResource;
import pt.up.fe.specs.binarytranslation.binarysegments.SegmentContext;
import pt.up.fe.specs.binarytranslation.graphs.edge.GraphInput;
import pt.up.fe.specs.binarytranslation.graphs.edge.GraphOutput;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.Replacer;

public class BinarySegmentGraphOutputUtils {

    /*
     * functional interface for any function which generates file
     * output for a given OutputStream (function should NOT close)
     * the stream on exit; that should be left to the calling wrapper
     * {@link #generateOutput(String filename, BinarySegmentGraphOutputGenerator func) generateOutput}
     */
    interface BinarySegmentGraphOutputGenerator {
        void apply(BinarySegmentGraph graph, OutputStream os);
    }

    /*
     * get file output stream with given filename
     * and specific output generator function via 
     * @BinarySegmentGraphOutputGenerator functional interface
     */
    private static void generateOutput(File outputfile,
            BinarySegmentGraph graph, BinarySegmentGraphOutputGenerator func) {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputfile);
            func.apply(graph, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 
     */
    public static void generateOutput(File parentfolder, BinarySegmentGraph graph) {

        var basename = "graph_" + Integer.toString(graph.getSegment().hashCode());

        // generate dotty (.dot)
        BinarySegmentGraphOutputUtils.generateOutput(
                new File(parentfolder, basename + ".dot"),
                graph, BinarySegmentGraphOutputUtils::printDotty);

        // render dotty (.png)
        BinarySegmentGraphOutputUtils.renderDotty(
                new File(parentfolder, basename + ".dot"), graph);

        // generate HTML summary
        BinarySegmentGraphOutputUtils.generateOutput(
                new File(parentfolder, basename + ".html"),
                graph, BinarySegmentGraphOutputUtils::printHTML);
    }

    /*
     * Print dotty to given outputstream (e.g., system.out, or file) 
     */
    public static void generateDotty(BinarySegmentGraph graph, OutputStream os) {
        printDotty(graph, os);
    }

    /*
     * Write to a given output stream (file or stdio)
     */
    private static void printDotty(BinarySegmentGraph graph, OutputStream os) {

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        try {
            bw.write("digraph G {\n\n");

            bw.write("graph [ dpi = 72, nodesep=\"0.1\" ];\n");
            bw.write("bgcolor=\"#ffffff00\";\n\n");

            // livein nodes
            bw.write("{ rank = source;\n");
            for (GraphInput in : graph.getLiveins()) {
                var s = in.getRepresentation();
                bw.write("\t\"in_" + s
                        + "\"[shape = box, fillcolor=\"#8080ff\", style=filled, label=\"" + s
                        + "\"];\n");
            }
            bw.write("}\n");

            bw.write("{ rank = sink;\n");
            // liveout nodes
            for (GraphOutput out : graph.getLiveouts()) {
                var s = out.getRepresentation();
                bw.write("\t\"out_" + s
                        + "\"[shape = box, fillcolor=\"#ff8080\", style=filled, label=\"" + s
                        + "\"];\n");
            }
            bw.write("}\n");

            bw.write("subgraph nodes {\n\tnode [style=filled, fillcolor=\"#ffffff\"];\n");
            for (GraphNode n : graph.getNodes()) {
                bw.write(n.rawDotty());
            }
            bw.write("}\n");

            for (int rank = 0; rank < graph.getCpl(); rank++) {
                bw.write("{ rank = same;\n");
                for (GraphNode n : graph.getNodes()) {
                    if (n.getLevel() == rank)
                        bw.write("\t\"" + n.getRepresentation() + "\"\n");
                }
                bw.write("}\n");
            }
            bw.write("}\n");
            bw.flush();
            bw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * Render the dotty file into a PNG file (calls dot executable)
     */
    private static void renderDotty(File dotfile, BinarySegmentGraph graph) {

        var dotpath = dotfile.getAbsolutePath();
        var pngpath = dotpath.replace(".dot", ".png");

        // render dotty
        var arguments = Arrays.asList(BinaryTranslationResource.DOTTY_BINARY.getResource(),
                "-Tpng", dotfile.getAbsolutePath(), "-o", pngpath);

        ProcessBuilder pb = new ProcessBuilder(arguments);

        // dot -Tps filename.dot -o outfile.ps
        Process proc = null;
        try {
            pb.directory(new File("."));
            pb.redirectErrorStream(true); // redirects stderr to stdout
            proc = pb.start();

        } catch (IOException e) {
            throw new RuntimeException("Could not run process bin with name: " + proc);
        }

        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * 
     */
    private static void printHTML(BinarySegmentGraph graph, OutputStream os) {

        ResourceProvider htmltmpl = BinaryTranslationResource.GRAPH_HTML_TEMPLATE;
        var htmlplage = new Replacer(htmltmpl);

        var seg = graph.getSegment();

        // Summary
        htmlplage.replace("<SEGMENTTYPE>", graph.getType().toString());
        htmlplage.replace("<NUMNODES>", Integer.toString(graph.getNodes().size()));
        htmlplage.replace("<NUMREADS>", Integer.toString(graph.getNumLoads()));
        htmlplage.replace("<NUMWRITES>", Integer.toString(graph.getNumStores()));
        htmlplage.replace("<MAXILP>", Integer.toString(graph.getMaxwidth()));
        htmlplage.replace("<CPL>", Integer.toString(graph.getCpl()));
        htmlplage.replace("<STATICCOVERAGE>", Float.toString(seg.getStaticCoverage()));
        htmlplage.replace("<DYNAMICCOVERAGE>", Float.toString(seg.getDynamicCoverage()));
        htmlplage.replace("<INITIATIONINTERVAL>",
                (graph.getType() == BinarySegmentGraphType.cyclical) ? graph.getInitiationInterval() : "N/A");
        htmlplage.replace("<IPC>", Float.toString(graph.getEstimatedIPC()));

        // Program info
        htmlplage.replace("<APPNAME>", seg.getAppinfo().getAppName());

        var compileinfo = seg.getAppinfo().getCompilationInfo();
        compileinfo.replace(" -", "<br>-");
        htmlplage.replace("<COMPILEINFO>", compileinfo);

        // Segment dump
        var segtext = seg.getRepresentation();
        htmlplage.replace("<SEGMENT>",
                segtext.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));

        // Contexts
        var contextString = "";
        for (SegmentContext context : seg.getContexts()) {
            contextString += context.getRepresentation() + "\n\n";
        }
        htmlplage.replace("<CONTEXTS>",
                contextString.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));

        // Graph
        var bitmapfilename = "graph_" + Integer.toString(graph.getSegment().hashCode()) + ".png";
        htmlplage.replace("<IMAGEFILE>", bitmapfilename);

        // git commit
        htmlplage.replace("<GITDESCRIPTION>", BinaryTranslationUtils.getCommitDescription());

        // date of generation
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        htmlplage.replace("<GENERATIONDATE>", formatter.format(new Date(System.currentTimeMillis())));

        // copyright year
        htmlplage.replace("<THEYEAR>", LocalDateTime.now().getYear());

        // write to file
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(htmlplage.toString());
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
