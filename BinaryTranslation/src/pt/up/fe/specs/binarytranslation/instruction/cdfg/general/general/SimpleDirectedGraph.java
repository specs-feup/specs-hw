/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;

/** A more complete version of JGrapht's SimpleDirectedGraph
 * 
 * @author João Conceição
 *
 * @param <V> Vertex of the graph
 * @param <E> Edge of the graph
 */

public class SimpleDirectedGraph<V,E> extends org.jgrapht.graph.SimpleDirectedGraph<V, E>{
  
    public SimpleDirectedGraph(Class<? extends E> EdgeClass){
        super(EdgeClass);
    }
    
    /** Adds a List of vertices to the graph
     * 
     * @param vertices List of vertices to be added
     */
    public void addVertices(List<V> vertices) {
        vertices.forEach((vertex) -> {this.addVertex(vertex);});
    }
    
    /** Adds a source vertex, adds a List of target vertices and adds generic edges between the source and targets
     * 
     * @param source Vertex were the edges originate from
     * @param targets Target vertices for the created edges
     */
    public void addVerticesAfter(V source, List<V> targets) {
        this.addVertex(source);
        this.addVertices(targets);
        this.addOutgoingEdgesTo(source, targets);
    }
    
    /** Adds a source vertex, adds a List of target vertices and adds the edges from the Map between the source and targets
     * 
     * @param source Vertex were the edges originate from
     * @param targets Map of vertices and the corresponding edges to be added between them and the source vertex
     */
    public void addVerticesAfter(V source, Map<V,E> targets_and_edges) {
        this.addVertex(source);
        this.addVertices(new ArrayList<V>(targets_and_edges.keySet()));
        targets_and_edges.forEach((target, edge) -> {this.addEdge(source, target, edge);});
    }
    
    /** Adds a List of source vertices, adds a target vertex and adds generic edges between the sources and the target
     * 
     * @param sources Vertices where the edges originate from
     * @param target Target vertex for the created edges
     */
    public void addVerticesBefore(List<V> sources, V target) {
        this.addVertex(target);
        this.addVertices(sources);
        this.addIncomingEdgesTo(target, sources);
    }
    
    public void addVerticesBefore(Map<V,E> sources_and_edges, V target) {
        this.addVertices(new ArrayList<V>(sources_and_edges.keySet()));
        this.addVertex(target);
        sources_and_edges.forEach((source, edge) -> {this.addEdge(source, target, edge);});
    }
    
    /** Replaces a vertex from the this graph with another one. All of the edges get copied to the new vertex.
     * 
     * @param current Vertex to be replaced 
     * @param replacement New vertex to be added
     */
    public void replaceVertex(V current, V replacement) {
        
        this.assertVertexExist(current);
        
        this.addVertex(replacement);
        
        this.addIncomingEdgesTo(replacement, this.getVerticesBefore(current));
        this.addOutgoingEdgesTo(replacement, this.getVerticesAfter(current));
        
        this.removeVertex(current);
    }
    
    public List<V> getVerticesBefore(V vertex){
        return Graphs.predecessorListOf(this, vertex);
    }
    
    public List<V> getVerticesAfter(V vertex){
        return Graphs.successorListOf(this, vertex);
    }
    
    public void addIncomingEdgesTo(V target, List<V> sources){   
        sources.forEach((source) -> {this.addEdge(source, target);});
    }
    
    
    public void addOutgoingEdgesTo(V source, List<V> targets) {
        targets.forEach((target) -> {this.addEdge(source, target);});
    }
    
    
    
}
