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

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;

public abstract class AGraphTransform {
    protected Graph<BtfVertex, DefaultEdge> graph;
    
    public AGraphTransform(Graph<BtfVertex, DefaultEdge> graph) {
        this.graph = graph;
    }
    
    public Graph<BtfVertex, DefaultEdge> applyToGraph() {
        applyTransform(graph);
        return graph;
    }
    
    public Graph<BtfVertex, DefaultEdge> applyToCopy() {
        Graph<BtfVertex, DefaultEdge> copy = new DefaultDirectedGraph<>(DefaultEdge.class);
        Graphs.addGraph(copy, graph);
        applyTransform(copy);
        return copy;
    }
    
    protected abstract Graph<BtfVertex, DefaultEdge> applyTransform(Graph<BtfVertex, DefaultEdge> g);
}
