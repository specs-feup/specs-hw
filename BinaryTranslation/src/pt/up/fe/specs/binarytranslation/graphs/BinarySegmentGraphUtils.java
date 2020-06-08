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

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import pt.up.fe.specs.binarytranslation.*;
import pt.up.fe.specs.binarytranslation.binarysegments.SegmentContext;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.Replacer;

public class BinarySegmentGraphUtils {

    /*
     * functional interface for any function which generates file
     * output for a given OutputStream (function should NOT close)
     * the stream on exit; that should be left to the calling wrapper
     * {@link #generateOutput(String filename, BinarySegmentGraphOutputGenerator func) generateOutput}
     */
    interface BinarySegmentGraphOutputGenerator {
        void apply(BinarySegmentGraph graph, OutputStream os);
    }

    // quick hack fix
    static String outputfolder;

    public static void generateOutput(BinarySegmentGraph graph, String parentfolder) {

        // output folder
        outputfolder = null;
        if (parentfolder != null)
            outputfolder = parentfolder;
        else
            outputfolder = "./output/";

        var f = new File(getOutputFolder(graph));
        f.mkdirs();

        // generate dotty and render dotty
        generateDotty(graph, getDotPathname(graph));

        // generate HTML summary
        generateHTML(graph, getHTMLPathname(graph));
    }

    /*
     * Return full path for output folder (includes upper most parent)
     */
    private static String getOutputFolder(BinarySegmentGraph graph) {
        var foldername = graph.getSegment().getAppinfo().getAppName();
        foldername = foldername.substring(0, foldername.lastIndexOf('.'));
        return outputfolder + "/" + foldername + "/graph_"
                + Integer.toString(graph.getSegment().hashCode());
    }

    /*
     * Return full path for HTML file
     */
    private static String getHTMLPathname(BinarySegmentGraph graph) {
        return getOutputFolder(graph) + "/graph_"
                + Integer.toString(graph.getSegment().hashCode()) + ".html";
    }

    /*
     * Return only the filename for DOT file
     */
    private static String getDotFilename(BinarySegmentGraph graph) {
        return "graph_" + Integer.toString(graph.getSegment().hashCode()) + ".dot";
    }

    /*
     * Return full path for dot file
     */
    private static String getDotPathname(BinarySegmentGraph graph) {
        return getOutputFolder(graph) + "/" + getDotFilename(graph);
    }

    /*
     * Return only the filename for PNG file
     */
    private static String getBitmapFilename(BinarySegmentGraph graph) {
        return getDotFilename(graph).replaceFirst(".dot", ".png");
    }

    /*
     * Return full path for PNG file
     */
    private static String getBitmapPathname(BinarySegmentGraph graph) {
        return getDotPathname(graph).replaceFirst(".dot", ".png");
    }

    /*
     * Print dotty to specified output file (with full path)
     */
    private static void generateDotty(BinarySegmentGraph graph, String filename) {
        BinarySegmentGraphUtils.generateOutput(graph,
                filename, BinarySegmentGraphUtils::printDotty);

        BinarySegmentGraphUtils.renderDotty(graph);
    }

    /*
     * Print dotty to specified output file (with full path)
     */
    private static void generateHTML(BinarySegmentGraph graph, String filename) {
        BinarySegmentGraphUtils.generateOutput(graph,
                filename, BinarySegmentGraphUtils::printHTML);
    }

    /*
     * Print dotty to given outputstream (e.g., system.out, or file) 
     */
    public static void generateDotty(BinarySegmentGraph graph, OutputStream os) {
        printDotty(graph, os);
    }

    /*
     * get file output stream with given filename
     * and specific output generator function via 
     * @BinarySegmentGraphOutputGenerator functional interface
     */
    private static void generateOutput(BinarySegmentGraph graph,
            String filename, BinarySegmentGraphOutputGenerator func) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
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
            for (String s : graph.getLiveins()) {
                bw.write("\t\"in_" + s
                        + "\"[shape = box, fillcolor=\"#8080ff\", style=filled, label=\"" + s
                        + "\"];\n");
            }
            bw.write("}\n");

            bw.write("{ rank = sink;\n");
            // liveout nodes
            for (String s : graph.getLiveouts()) {
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
    private static void renderDotty(BinarySegmentGraph graph) {

        // render dotty
        var arguments = Arrays.asList(BinaryTranslationResource.DOTTY_BINARY.getResource(),
                "-Tpng", getDotPathname(graph), "-o", getBitmapPathname(graph));

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
        htmlplage.replace("<IMAGEFILE>", getBitmapFilename(graph));

        // git commit
        htmlplage.replace("<GITDESCRIPTION>", BinaryTranslationUtils.getCommitDescription());

        // date of generation
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        htmlplage.replace("<GENERATIONDATE>", formatter.format(new Date(System.currentTimeMillis())));

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
