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
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
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
    }

    /*
     * Depth of graph
     */
    public int getCpl() {
        return this.cpl;
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
     * Write to a given output stream (file or stdio)
     */
    private void printDotty(OutputStream os) {

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        try {
            bw.write("digraph G {\n");

            // livein nodes
            for (String s : this.liveins) {
                bw.write("\t\"in_" + s + "\"[shape = box, label=\"" + s + "\"];\n");
            }

            // liveout nodes
            for (String s : this.liveouts) {
                bw.write("\t\"out_" + s + "\"[shape = box, label=\"" + s + "\"];\n");
            }

            for (GraphNode n : this.nodes) {
                bw.write(n.rawDotty());
            }
            bw.write("}\n");
            bw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
