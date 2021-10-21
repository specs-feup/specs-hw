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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph;

import java.util.List;

import org.jgrapht.Graphs;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.GenericCDFG;

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
    
    public void addVertexes(List<V> vertexes) {     
        vertexes.forEach((vertex) -> {this.addVertex(vertex);});
    }
    
    /** Replaces the current vertex with the replacement vertex<br>
     *  It insertss the replacement vertex into the graph, copies all of the current node's edges and it removes it from the graph
     * @param current Vertex to be remove
     * @param replacement Vertex to replace the current vertex
     */
    public void replaceVertex(V current, V replacement) {
        
        this.assertVertexExist(current);
        
        this.addVertex(replacement);
        
        this.addIncomingEdgesTo(replacement, this.getVertexesBefore(current));
        this.addOutgoingEdgesTo(replacement, this.getVertexesAfter(current));
        
        this.removeVertex(current);
    }
    
    /** Return a List of the vertextes before the argument vertex
     * 
     * @param vertex 
     * @return A List of the vertexes before the argument vertex
     */
    public List<V> getVertexesBefore(V vertex){
        return Graphs.predecessorListOf(this, vertex);
    }
    
    public List<V> getVertexesAfter(V vertex){
        return Graphs.predecessorListOf(this, vertex);
    }
    
    public void addIncomingEdgesTo(V target, List<V> sources){
        
        sources.forEach((source) -> {
            this.addEdge(source, target);
            });
        
        Graphs.addIncomingEdges(this, target, sources);
    }
    
    public void addOutgoingEdgesTo(V source, List<V> targets) {
        Graphs.addOutgoingEdges(this, source, targets);
    }
    

    
}
