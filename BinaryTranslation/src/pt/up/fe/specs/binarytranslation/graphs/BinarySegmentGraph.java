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

import java.util.*;

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
     * Type
     */
    public BinarySegmentGraphType getType() {
        return type;
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
     * 
     */
    public Set<String> getLiveins() {
        return liveins;
    }

    /*
     * 
     */
    public Set<String> getLiveouts() {
        return liveouts;
    }

    /*
     * 
     */
    public List<GraphNode> getNodes() {
        return nodes;
    }

    /*
     * 
     */
    public int getNumLoads() {
        return this.numloads;
    }

    /*
     * 
     */
    public int getNumStores() {
        return numstores;
    }

    /*
     * 
     */
    public int getMaxwidth() {
        return maxwidth;
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
        BinarySegmentGraphUtils.generateDotty(this, System.out);
    }

    /*
     * Generate a folder and output all relevant info for this graph into it
     * but use current working dir as parent directory
     */
    public void generateOutput() {
        generateOutput(null);
    }

    /*
     * Generate a folder and output all relevant info for this graph into it
     */
    public void generateOutput(String parentfolder) {
        BinarySegmentGraphUtils.generateOutput(this, parentfolder);
    }
}
