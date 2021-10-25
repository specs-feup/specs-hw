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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.Graphs;

public class GeneralFlowGraph<V,E> extends SimpleDirectedGraph<V,E>{

    private VertexList<V> inputs;
    private VertexList<V> outputs; 
    
    /** Creates a new GeneralFlowGraph
     * 
     * @param EdgeClass super class of the edges to be used (required by JGrapht)
     */
    public GeneralFlowGraph(Class<? extends E> EdgeClass) {
        super(EdgeClass);
        
        this.inputs = new VertexList<V>();
        this.outputs = new VertexList<V>(); 
       
    }

    @Override
    public boolean removeVertex(V vertex) {
        
        this.removeInput(vertex);
        this.removeOutput(vertex);
        
        return super.removeVertex(vertex);
    }
   
    @Override
    public void replaceVertex(V current, V replacement) {
        
        this.getInputVertexList().replace(current);
        this.getOutputVertexList().replace(current);
        
        super.replaceVertex(current, replacement);
    }

    /** Returns a List of vertices that evaluate true to the argument predicate
     * 
     * @param predicate Predicate to be evaluated
     * @return List of vertices that evaluate true
     */
    public List<V> getVerticesOfPredicate(Predicate<V> predicate){
        return this.getVertexStreamOfPredicate(predicate).collect(Collectors.toList());
    }
    
    /** Returns a Stream of vertices that evaluate true to the argument predicate
     * 
     * @param predicate Predicate to be evaluated
     * @return Stream of vertices that evaluate true
     */
    public Stream<V> getVertexStreamOfPredicate(Predicate<V> predicate){
        return this.vertexSet().stream().filter(predicate);
    }
    
    /** Generates and adds the inputs of this graph. <br>
     *  A vertex is considered an input if it does not have any incoming edges
     */
    public void generateInputs() {
        this.setInputs(this.getVerticesOfPredicate(v -> this.incomingEdgesOf(v).isEmpty()));
    }
    
    /** Generates and adds the outputs of this graph. <br>
     * A vertex is considered an output if it does not have any outgoing edges
     */
    public void generateOutputs() {      
       this.setOutputs(this.getVerticesOfPredicate(v -> this.outgoingEdgesOf(v).isEmpty()));
    }
    
    /** Returns a List of the graph's input vertices
     * 
     * @return List of the graph's input vertices
     */
    public List<V> getInputs(){
        return this.inputs;
    }
    
    public VertexList<V> getInputVertexList(){
        return this.inputs;
    }
    
    /** Sets the graph's input vertices List to the parameter's List<br>
     * The parameter's vertices must have been already added to the graph
     * @param vertices New List of outputs
     */
    public void setInputs(List<V> vertices) {
        this.getInputVertexList().set(vertices);
    }
    
    /** Adds a vertex to the graph's input vertices List, only if the vertex does not exist
     * 
     * @param vertex Vertex to be added
     */
    public void addInput(V vertex) {
       this.assertVertexExist(vertex);
       this.replaceVertex(vertex, this.getInput(vertex));
       this.getInputVertexList().add(vertex);
        
    }
    
    /** Returns the vertex from the graph's input vertices list that the equals() method of the vertices evaluates true against the argument vertex.<br>
     * 
     * This methods only makes sense if the equals() method of the vertices is overridden, otherwise this method is equivalent to hasInput() but it returns the vertex instead of a Boolean
     * @param vertex Vertex to be checked
     * @return The vertex found or null if no vertex is found
     */
    public V getInput(V vertex) {
        return this.getInputVertexList().get(vertex);
    }
    
    /** Checks if the graph has the argument vertex in the graph's input vertices List.<br>
     * 
     * @param vertex Vertex to be checked
     * @return True if the graph has the argument vertex in the inputs list, otherwise false
     */
    public boolean hasInput(V vertex) {
        return this.getInputVertexList().contains(vertex);
    }
    
    /** Removes the argument vertex from the graph's input vertices List.<br>
     *  <b>Note: this does not remove the vertex from the graph itself, only the outputs list of the graph</b>
     * 
     * @param vertex Vertex to be removed
     */
    public void removeInput(V vertex) {
        this.getInputVertexList().remove(vertex);
    }
    
    /** Returns a List of the graph's output vertices
     * 
     * @return List of the graph's output vertices
     */
    public List<V> getOutputs(){
        return this.outputs;
    }
    
    public VertexList<V> getOutputVertexList(){
        return this.outputs;
    }
    
    /** Sets the graph's output vertices List to the parameter's List<br>
     * The parameter's vertices must have been already added to the graph
     * @param vertices New List of outputs
     */
    public void setOutputs(List<V> vertices) {
        this.getOutputVertexList().add(vertices);
    }
    
    /** Adds a vertex of the graph to the graph's outputs List, only if the vertex does not exist
     * 
     * @param vertex Vertex to be added
     */
    public void addOutput(V vertex) {
        this.assertVertexExist(vertex);
        this.getOutputVertexList().add(vertex);
    }
    
    /** Returns the vertex from the graph's outputs list that the equals() method of the vertices evaluates true against the argument vertex.<br>
     * 
     * This methods only makes sense if the equals() method of the vertices is overridden, otherwise this method is equivalent to hasOutput() but it returns the vertex instead of a Boolean
     * @param vertex Vertex to be checked
     * @return The vertex found or null if no vertex is found
     */
    public V getOutput(V vertex) {
        return this.getOutputVertexList().get(vertex);
    }
    
    /** Checks if the graph has the argument vertex in the graph's output vertices List.<br>
     * 
     * @param vertex Vertex to be checked
     * @return True if the graph has the argument vertex in the outputs list, otherwise false
     */
    public boolean hasOutput(V vertex) {
        return this.getOutputVertexList().contains(vertex);
    }
    
    /** Removes the argument vertex from the graph's output vertices List.<br>
     *  <b>Note: this does not remove the vertex from the graph itself, only the outputs list of the graph</b>
     * 
     * @param vertex Vertex to be removed
     */
    public void removeOutput(V vertex) {
        this.getOutputVertexList().remove(vertex);
    }
    
    public V getFirstOutput() {
        return this.getOutputs().get(0);
    }

    public V getFirstInput() {
        return this.getInputs().get(0);
    }
    
    /** Merges the argument vertices List with the argument vertex.
     * <br>
     * This copies all of the edges from the vertices List to the argument vertex and removes the argument vertices List from the graph
     * 
     * @param vertex Vertex that vertices List merges with
     * @param vertices Vertices to be merges and removed from the graph
     * @throws IllegalArgumentException If any of the argument's vertices are not part of the graph
     */
    public void mergeVertices(V vertex , List<V> vertices) throws IllegalArgumentException{
         
        this.assertVertexExist(vertex); 
        vertices.forEach(v -> this.assertVertexExist(v));
        
        vertices.forEach(v -> {
            
            this.incomingEdgesOf(v).forEach(e -> {this.addEdge(this.getEdgeSource(e), vertex);});
            this.outgoingEdgesOf(v).forEach(e -> {this.addEdge(vertex, this.getEdgeTarget(e));});
            
            this.removeVertex(v);
            
        });
        
    }
    
    /** Removes a vertex but keeps all of it's edges. All of the incoming edges sources get connected to all of the outgoing edges targets
     * 
     * @param vertex Vertex to be suppressed
     */
    public void suppressVertex(V vertex) {

        this.incomingEdgesOf(vertex).forEach((in_edge) -> {
            this.outgoingEdgesOf(vertex).forEach((out_edge) -> {
                this.addEdge(this.getEdgeSource(in_edge), this.getEdgeTarget(out_edge));
            });
        });
  
        this.removeVertex(vertex);
    }
    
    /** Returns the operands(vertices) before a vertex.<br>
     * 
     * This method is kind of redundant
     * 
     * @param vertex Vertex to get the operands of
     * @return A List of the operands(vertices) of the argument vertex
     */
    public List<V> getOperandsOf(V vertex){
        
        List<V> operand_list = new ArrayList<V>();
        
        
        this.incomingEdgesOf(vertex).forEach(e -> operand_list.add(this.getEdgeSource(e)));
        
        return operand_list;
    }
    
    @Deprecated
    public GeneralFlowGraph<V,E> mergeGraph(GeneralFlowGraph<V,E> graph) {
        
            // Joins the new graph to the current one
           Graphs.addGraph(this, graph);
    
           
           
      // Merge this graph output nodes that output to graph input nodes, and add the independent input nodes of graph to this graph's inputs
         /*  for(V vertex : graph.getInputs()) {

              
              if(this.hasOutput(vertex)) {   
    
                  this.replaceVertex(graph.getInput(vertex), this.getOutput(vertex)); 
                  
                  this.removeNodeAndJoinEdges(this.getOutput(vertex));
                  this.outputs.remove(vertex);
      
              }else {
      
                  if(this.hasInput(vertex)) {
                      this.replaceVertex(graph.getInput(vertex), this.getInput(vertex));
                  }else{   
                      System.out.println(vertex);
                      this.addInput(vertex);
                  }
              }

          }
          */
           
           
           graph.getInputs().forEach(v -> {
               
               if(this.hasOutput(v)) {
                   //super.replaceVertex(v, this.getOutput(v));
               }
               
               if(this.hasInput(v)) {
                   this.mergeVertices(this.getInput(v), List.of(v));
  
               }else {
                   this.addInput(v);
               }
               
           });
           
          // merges the output nodes of both graphs
          
          graph.getOutputs().forEach(vertex -> {
              
              if(!this.hasOutput(vertex)) {   
                  this.addOutput(vertex);
               }else {
                   this.replaceVertex(graph.getOutput(vertex), this.getOutput(vertex));
               }
              
          });

          
          
           return this;
       }
        
}
