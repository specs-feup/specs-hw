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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private int numnodes;
    private int cpl;
    private int maxwidth, minwidth;
    private int numstores, numloads;
    BinarySegment seg;
    List<GraphNode> nodes;

    /*
     * Which nodes are connected to which
     * <child, List of parents>
     */
    private Map<GraphNode, List<GraphNode>> adjacencyTable;

    /*
     * Map input registers to nodes which have written over them?
     */
    private Map<String, GraphNode> lastwriter;

    public BinarySegmentGraph(BinarySegment seg) {
        this.numloads = 0;
        this.numstores = 0;
        this.seg = seg;
        this.nodes = new ArrayList<GraphNode>();

        // get symbolic instruction list
        int nodecounter = 0;
        var insts = seg.getInstructions();
        for (Instruction i : insts) {

            // add all instructions to node list, convert instruction to proper node
            // also converts instruction AOperands into GraphInputs (TODO: conversion is still incomplete)
            var node = new GraphNode(i);
            node.setNodenr(nodecounter++);
            this.nodes.add(node);
        }

        // create empty lists for adjacency table
        this.adjacencyTable = new HashMap<GraphNode, List<GraphNode>>();
        for (GraphNode n : this.nodes) {
            this.adjacencyTable.put(n, new ArrayList<GraphNode>());
        }

        // bubble sort?
        // 1. analyze nodes in order of insertion
        // mark registers which have been written too?
        this.lastwriter = new HashMap<String, GraphNode>();
        for (GraphNode n : this.nodes) {

            // replace this nodes inputs with previous outputs, if any
            for (GraphInput in : n.getInputs()) {
                var inputval = in.getValue();
                if (lastwriter.containsKey(inputval)) {

                    // producer node ID
                    var producer = lastwriter.get(inputval);
                    var value = producer.getNodeNr();
                    in.setInputAs(GraphInputType.noderesult, Integer.toString(value));

                    // set level
                    if (n.getLevel() <= producer.getLevel())
                        n.setLevel(producer.getLevel() + 1);

                    // update adjacency table
                    this.adjacencyTable.get(n).add(producer);
                }
            }

            // update lastwriter table
            for (GraphOutput out : n.getOutputs()) {
                lastwriter.put(out.getValue(), n);
            }
        }

        // count final stuff
        // e.g. numstores, numloads
        for (GraphNode n : this.nodes) {

            if (n.getInst().isLoad())
                this.numloads++;

            else if (n.getInst().isStore())
                this.numstores++;
        }

        // compute CPL, and ILP, and expected speedup (depends on segment type and target hw?)
    }

    /*
     * Tester function to print this graph as a dotty, into the console
     */
    public void printDotty() {

        System.out.print("digraph G {\n");
        for (GraphNode n : this.adjacencyTable.keySet()) {

            // connections to self from register inputs
            for (GraphInput in : n.getInputs()) {
                if (in.getType() != GraphInputType.noderesult) {
                    System.out.print("\t" + in.getValue() + " -> " + n.getRepresentation() + ";\n");
                }
            }

            // connections to children
            var children = this.adjacencyTable.get(n);
            for (GraphNode c : children) {
                System.out.print("\t" + n.getRepresentation() + " -> " + c.getRepresentation() + ";\n");
            }
        }
        System.out.print("}\n");
    }
}
