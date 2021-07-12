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

package pt.up.fe.specs.binarytranslation.analysis.graphs.transforms;

import java.util.ArrayList;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;

public class TransformRemoveZeroLatencyOps extends AGraphTransform {

    public TransformRemoveZeroLatencyOps(Graph<BtfVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<BtfVertex, DefaultEdge> applyTransform(Graph<BtfVertex, DefaultEdge> g) {
        var toRemove = new ArrayList<BtfVertex>();

        for (var v : g.vertexSet()) {
            if (v.getType() == BtfVertexType.IMMEDIATE || v.getType() == BtfVertexType.JUMP)
                toRemove.add(v);
            if (v.getType() == BtfVertexType.REGISTER) {
                if (g.inDegreeOf(v) == 1 && g.outDegreeOf(v) > 1) {
                    var inVertices = Graphs.predecessorListOf(g, v);
                    var outVertices = Graphs.successorListOf(g, v);
                    toRemove.add(v);
                    for (var inV : inVertices) {
                        for (var outV : outVertices)
                            g.addEdge(inV, outV);
                    }
                }
                if (g.outDegreeOf(v) == 0 || g.inDegreeOf(v) == 0) {
                    toRemove.add(v);
                }
            }
        }
        g.removeAllVertices(toRemove);
        return g;
    }

}
