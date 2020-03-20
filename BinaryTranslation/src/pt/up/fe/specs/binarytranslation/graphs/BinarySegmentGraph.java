/**
 * Copyright 2019 SPeCS.
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.BinaryTranslationResource;
import pt.up.fe.specs.binarytranslation.BinaryTranslationUtils;
import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.SegmentContext;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * This class represents a binary segment as a graph, by hosting the instructions of the segment as the data transported
 * by graph nodes
 * 
 * @author nuno
 *
 */
public class BinarySegmentGraph {

    private BinarySegmentGraphType type;
    private int numnodes;
    private int cpl;
    private int maxwidth;
    private int numstores, numloads;
    private int initiationInterval = 1; // 1 for non cyclical segments
    private float estimatedIPC = -1;
    BinarySegment seg;
    List<GraphNode> nodes;

    // representation strings of liveins and liveouts; computed after graph is resolved
    Set<String> liveins;
    Set<String> liveouts;

    /*
     * Which nodes are connected to which: <child, List of parents>
     */
    private Map<GraphNode, List<GraphNode>> adjacencyTable;

    /*
     * ILP per graph level
     */
    private List<Integer> ilp;

    /*
     * Constructor
     */
    private BinarySegmentGraph(BinarySegment seg, List<GraphNode> nodes,
            Map<GraphNode, List<GraphNode>> adjacencyTable, Set<String> liveins, Set<String> liveouts) {

        // core information
        this.seg = seg;

        // segment super type
        if (this.seg.getSegmentType().isCyclical())
            this.type = BinarySegmentGraphType.cyclical;

        else if (this.seg.getSegmentType().isAcyclical())
            this.type = BinarySegmentGraphType.acyclical;

        this.nodes = nodes;
        this.adjacencyTable = adjacencyTable;
        this.liveins = liveins;
        this.liveouts = liveouts;
        this.numnodes = nodes.size();

        // compute stuff based on nodes
        for (GraphNode n : nodes) {

            if (n.getInst().isLoad())
                this.numloads++;

            else if (n.getInst().isStore())
                this.numstores++;

            // CPL
            if (n.getLevel() > this.cpl)
                this.cpl = n.getLevel();
        }

        // account for 0
        this.cpl++;

        // init ILP counters
        this.ilp = new ArrayList<Integer>();
        for (int i = 0; i < this.cpl; i++)
            this.ilp.add(0);

        // ILP
        for (GraphNode n : this.nodes) {
            var count = this.ilp.get(n.getLevel());
            this.ilp.set(n.getLevel(), count + 1);
        }

        // max ilp
        for (Integer i : this.ilp)
            if (i > this.maxwidth)
                this.maxwidth = i;

        // initiation interval
        if (this.type == BinarySegmentGraphType.cyclical) {
            for (GraphNode n : this.nodes) {
                if (n.getInst().isBackwardsJump())
                    if (this.initiationInterval < n.getLevel() + 1)
                        this.initiationInterval = n.getLevel() + 1;
            }
        }

        // estimated IPC
        this.estimatedIPC = (float) this.numnodes / (float) this.initiationInterval;
    }

    /*
     * Depth of graph
     */
    public int getCpl() {
        return this.cpl;
    }

    /*
     * Get originating segment
     */
    public BinarySegment getSegment() {
        return this.seg;
    }

    /*
     * initiation interval; applicable for cyclical loops, equals 1 for acyclical loops
     */
    public int getInitiationInterval() {
        return this.initiationInterval;
    }

    /*
     * 
     */
    public float getEstimatedIPC() {
        return estimatedIPC;
    }

    /*
     * Static "constructor"
     */
    public static BinarySegmentGraph newInstance(BinarySegment seg) {

        /*
         * Which nodes are connected to which: <child, List of parents>
         */
        Map<GraphNode, List<GraphNode>> adjacencyTable;

        /*
         * Map input registers to nodes which have written over them?
         */
        Map<String, GraphNode> lastwriter;

        /*
         * Nodes of the graph (typically one per instruction)
         */
        List<GraphNode> nodes = new ArrayList<GraphNode>();

        // get symbolic instruction list
        int nodecounter = 0;
        var insts = seg.getInstructions();
        for (Instruction i : insts) {

            // add all instructions to node list, convert instruction to proper node
            // also converts instruction AOperands into GraphInputs (TODO: conversion is still incomplete)
            var node = new GraphNode(i);
            node.setNodenr(nodecounter++);
            nodes.add(node);
        }

        // create empty lists for adjacency table
        adjacencyTable = new HashMap<GraphNode, List<GraphNode>>();
        for (GraphNode n : nodes) {
            adjacencyTable.put(n, new ArrayList<GraphNode>());
        }

        // analyze nodes in order of insertion
        // mark registers which have been written too
        lastwriter = new HashMap<String, GraphNode>();
        for (GraphNode n : nodes) {

            // replace this nodes inputs with previous outputs, if any
            for (GraphInput in : n.getInputs()) {
                var inputval = in.getRepresentation();
                if (lastwriter.containsKey(inputval)) {

                    // producer node ID; set node input as producer node instead of livein
                    var producer = lastwriter.get(inputval);
                    in.setInputAs(GraphInputType.noderesult, producer.getRepresentation());

                    // set level
                    if (n.getLevel() <= producer.getLevel())
                        n.setLevel(producer.getLevel() + 1);

                    // update adjacency table
                    adjacencyTable.get(n).add(producer);
                }
            }

            // update lastwriter table
            for (GraphOutput out : n.getOutputs()) {
                lastwriter.put(out.getRepresentation(), n);
            }
        }

        // set graphoutputs as liveouts based on final result of "lastwriter"
        for (GraphNode n : nodes) {
            for (GraphOutput out : n.getOutputs()) {
                if (lastwriter.get(out.getRepresentation()) == n) {
                    out.setOutputAs(GraphOutputType.liveout, out.getRepresentation());
                }
            }
        }

        // init liveins list
        Set<String> liveins = new LinkedHashSet<String>();
        for (GraphNode n : nodes) {
            for (GraphInput in : n.getInputs()) {
                if (in.isLivein()) {
                    liveins.add(in.getRepresentation());
                }
            }
        }

        // init liveouts list
        Set<String> liveouts = new LinkedHashSet<String>();
        for (GraphNode n : nodes) {
            for (GraphOutput out : n.getOutputs()) {
                if (out.isLiveout())
                    liveouts.add(out.getRepresentation());
                // NOTE: value here equals representation, e.g. r<d>
            }
        }

        return new BinarySegmentGraph(seg, nodes, adjacencyTable, liveins, liveouts);
    }

    /*
     * Tester function to print this graph as a dotty, into the console
     */
    public void printDotty() {
        this.printDotty(System.out);
    }

    /*
     * Print dotty representation to file
     */
    public void printDotty(String filename) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
            this.printDotty(fos);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * Write to a given output stream (file or stdio)
     */
    private void printDotty(OutputStream os) {

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        try {
            bw.write("digraph G {\n\n");

            bw.write("graph [ dpi = 72, nodesep=\"0.1\" ];\n");
            bw.write("bgcolor=\"#ffffff00\";\n\n");

            // livein nodes
            bw.write("{ rank = source;\n");
            for (String s : this.liveins) {
                bw.write("\t\"in_" + s
                        + "\"[shape = box, fillcolor=\"#8080ff\", style=filled, label=\"" + s
                        + "\"];\n");
            }
            bw.write("}\n");

            bw.write("{ rank = sink;\n");
            // liveout nodes
            for (String s : this.liveouts) {
                bw.write("\t\"out_" + s
                        + "\"[shape = box, fillcolor=\"#ff8080\", style=filled, label=\"" + s
                        + "\"];\n");
            }
            bw.write("}\n");

            bw.write("subgraph nodes {\n\tnode [style=filled, fillcolor=\"#ffffff\"];\n");
            for (GraphNode n : this.nodes) {
                bw.write(n.rawDotty());
            }
            bw.write("}\n");

            for (int rank = 0; rank < this.cpl; rank++) {
                bw.write("{ rank = same;\n");
                for (GraphNode n : this.nodes) {
                    if (n.getLevel() == rank)
                        bw.write("\t\"" + n.getRepresentation() + "\"\n");
                }
                bw.write("}\n");
            }
            bw.write("}\n");
            bw.flush();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * Generatre a folder and output all relevant info for this graph into it
     */
    public void generateOutput() {

        // output folder
        var f = new File(this.getOutputFolder());
        f.mkdirs();

        // generate dotty
        // String dotfilename = foldername + "/" + "graph_" + Integer.toString(seg.hashCode()) + ".dot";
        this.printDotty(this.getOutputFolder() + "/" + this.getDotFilename());

        // render dotty
        this.renderDotty();

        // generate HTML summary
        this.printHTML();

        return;
    }

    /*
     * 
     */
    private void renderDotty() {

        // render dotty
        var arguments = Arrays.asList(BinaryTranslationResource.DOTTY_BINARY.getResource(),
                "-Tpng", this.getOutputFolder() + "/" + this.getDotFilename(),
                "-o", this.getOutputFolder() + "/" + this.getBitmapFilename());
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
    private void printHTML() {

        // TODO: Use Replacer instead

        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(this.getOutputFolder() + "/" + this.getHTMLFilename());
            bw = new BufferedWriter(new OutputStreamWriter(fos));

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            bw.write("<!DOCTYPE html>\r\n<html><head><style>\r\n" +
                    ".box {background-color:lightgrey; width:50%;}\r\n</style></head><body>\n\n");

            // Header info
            bw.write("<h1>Binary Segment Graph Information</h1>\n\n");

            // Summary
            bw.write("<div class=box>");
            bw.write("<h2>Summary</h2>\n\n");
            bw.write("Segment type: " + this.type.toString() + "<br>\n");
            bw.write("Number of nodes: " + Integer.toString(this.numnodes) + "<br>\n");
            bw.write("Number of memory reads: " + Integer.toString(this.numloads) + "<br>\n");
            bw.write("Number of memory writes: " + Integer.toString(this.numstores) + "<br>\n");
            bw.write("Maximum ILP of graph (widest row): " + Integer.toString(this.maxwidth) + "<br>\n");
            bw.write("Critical Path Length: " + Integer.toString(this.cpl) + "<br>\n");
            bw.write("Static Coverage: " + Float.toString(this.seg.getStaticCoverage()) + "<br>\n");
            bw.write("Dynamic Coverage: " + Float.toString(this.seg.getDynamicCoverage()) + "<br>\n");

            if (this.type == BinarySegmentGraphType.cyclical)
                bw.write("Initiation Interval: " + this.initiationInterval + "<br>\n");

            bw.write("Instructions per clock cycle (ideal): " + this.estimatedIPC + "<br>\n\n");
            bw.write("</div>");

            // Program info
            bw.write("<div class=box>");
            bw.write("<h2>Program information</h2>\n");
            bw.write("Program name: " + this.seg.getAppName() + "<br>\n");
            bw.write("</div>");

            bw.write("<div class=box>");
            bw.write("<h3>Compilation information:</h3>\n");
            var compileinfo = this.seg.getCompilationFlags();
            bw.write(compileinfo.replace(" -", "<br>-") + "<br>\n\n");
            bw.write("</div>");

            // Segment dump
            bw.write("<div class=box>");
            bw.write("<h2>Segment:</h2>\n");
            var segtext = this.seg.getRepresentation();
            bw.write(segtext.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
            bw.write("<br>\n");
            bw.write("</div>");

            // Contexts
            bw.write("<div class=box>");
            bw.write("<h3>Segment Contexts</h3>\n");
            for (SegmentContext context : this.seg.getContexts()) {
                bw.write(context.getRepresentation().replaceAll("<", "&lt;")
                        .replaceAll(">", "&gt;").replaceAll("\n", "<br>") + "<br>");
            }
            bw.write("</div>");

            // Graph
            bw.write("<div class=box>");
            bw.write("<h2>Segment Graph:</h2>\n");
            bw.write("<img src=\"" + this.getBitmapFilename() + "\"><br>\n\n");
            bw.write("</div>");

            bw.write("</body>\n</html>\n");

            // git commit
            String gitDescription = BinaryTranslationUtils.getCommitDescription();

            // date of generation
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());

            bw.write("<br><footer>&copy; Copyright 2019 SPeCS<br>"
                    + "Generated by Binary Translation Framework, " + gitDescription + "<br>"
                    + "Generation date: " + formatter.format(date) + "<br>"
                    + "Find the repository at <a href=\"https://github.com/specs-feup/specs-hw\">SPeCS Hardware</a><br>"
                    + "Binary Translation Framework developed at <a href=\"https://www.inesctec.pt/\">INESC TEC</a> (CTM and CSIG) and UP/FEUP<br>"
                    + "<br>Contact the developers:<br>"
                    + "<a href=\"mailto:nuno.m.paulino@inesctec.pt\">Dr. Nuno Paulino</a><br>"
                    + "<a href=\"mailto:joao.bispo@inesctec.pt\">Dr. João Bispo</a>"
                    + "</footer>");
            bw.flush();
            bw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getOutputFolder() {
        var foldername = this.seg.getAppName();
        foldername = foldername.substring(0, foldername.lastIndexOf('.'));
        return "./output/" + foldername + "/graph_" + Integer.toString(this.hashCode());
    }

    private String getHTMLFilename() {
        return "graph_" + Integer.toString(seg.hashCode()) + ".html";
    }

    private String getDotFilename() {
        return "graph_" + Integer.toString(seg.hashCode()) + ".dot";
    }

    private String getBitmapFilename() {
        return this.getDotFilename().replaceFirst(".dot", ".png");
    }
}
