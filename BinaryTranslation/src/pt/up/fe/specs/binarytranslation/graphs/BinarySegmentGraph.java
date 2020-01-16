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
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class BinarySegmentGraph {

    private int numnodes;
    private int cpl;
    private int maxwidth, minwidth;
    private int numstores, numloads;
    BinarySegment seg;
    List<GraphNode> nodes;

    /*
     * Which nodes are connected to which
     */
    private Map<GraphNode, List<GraphNode>> adjacencyTable;

    public BinarySegmentGraph(BinarySegment seg) {
        this.seg = seg;
        this.nodes = new ArrayList<GraphNode>();

        var insts = seg.getInstructions();
        for (Instruction i : insts) {
            // this.nodes.add(
        }
    }
}
