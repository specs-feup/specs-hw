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
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;

public class TransformRemoveTemporaryVertices extends AGraphTransform {
    private BtfVertexType type = BtfVertexType.REGISTER;

    public TransformRemoveTemporaryVertices(Graph<BtfVertex, DefaultEdge> graph) {
        super(graph);
    }

    public TransformRemoveTemporaryVertices(Graph<BtfVertex, DefaultEdge> graph, BtfVertexType type) {
        super(graph);
        this.type = type;
    }

    @Override
    protected Graph<BtfVertex, DefaultEdge> applyTransform(Graph<BtfVertex, DefaultEdge> g) {
        var regs = GraphUtils.getVerticesWithType(g, type);
        var toRemove = new ArrayList<BtfVertex>();

        for (var v : regs) {
            if (g.inDegreeOf(v) == 1 && g.outDegreeOf(v) == 1) {
                var inEdges = g.incomingEdgesOf(v);
                var inVertex = BtfVertex.nullVertex;
                for (var e : inEdges) {
                    inVertex = g.getEdgeSource(e);
                }

                var outEdges = g.outgoingEdgesOf(v);
                var outVertex = BtfVertex.nullVertex;
                for (var e : outEdges) {
                    outVertex = g.getEdgeTarget(e);
                }

                if (type == BtfVertexType.REGISTER) {
//                    if ((inVertex.getType() == AddressVertexType.OPERATION
//                            || inVertex.getType() == AddressVertexType.MEMORY) &&
//                            (outVertex.getType() == AddressVertexType.OPERATION
//                                    && outVertex.getType() == AddressVertexType.MEMORY)) {
                        toRemove.add(v);
                        graph.addEdge(inVertex, outVertex);
                    //}
                } else {
                    toRemove.add(v);
                    graph.addEdge(inVertex, outVertex);
                }
            }
        }
        for (var v : toRemove) {
            g.removeVertex(v);
        }
        return g;
    }

}
