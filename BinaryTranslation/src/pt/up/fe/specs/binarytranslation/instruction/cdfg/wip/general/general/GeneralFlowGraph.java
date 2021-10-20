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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.SimpleDirectedGraph;

public class GeneralFlowGraph<V,E> extends SimpleDirectedGraph<V,E>{

    private List<V> inputs;
    private List<V> outputs; 
    
    public GeneralFlowGraph(Class<? extends E> EdgeClass) {
        super(EdgeClass);
        
        this.inputs = new ArrayList<V>();
        this.outputs = new ArrayList<V>(); 
    }
    
    @Override
    public boolean removeVertex(V vertex) {
        
        this.removeInput(vertex);
        this.removeOutput(vertex);
        
        return super.removeVertex(vertex);
    }
   
    @Override
    public void replaceVertex(V old_node, V new_node) {

        this.incomingEdgesOf(old_node).forEach((edge) -> {this.addEdge(this.getEdgeSource(edge), new_node);});
        this.outgoingEdgesOf(old_node).forEach((edge) -> {this.addEdge(new_node,this.getEdgeTarget(edge));});
        
        if(this.hasInput(new_node)) {
            this.replaceFromIO(this.getInputs(), new_node);
        }
        if(this.hasOutput(new_node)) {
            this.replaceFromIO(this.getOutputs(), new_node);
        }
        
        this.removeVertex(old_node);
    }
    
    protected void replaceFromIO(List<V> io_list, V vertex) {

        io_list.remove(this.getFromIO(io_list, vertex));
        io_list.add(vertex);
 
    }

    protected V getFromIO(List<V> io_list, V vertex) {
        
        for(V n : io_list) {
            if(vertex.equals(n)) {
                return n;
            }
        }
        
        return null;
    }
    
    protected void removeFromIO(List<V> io_list, V vertex) {
        io_list.remove(this.getFromIO(io_list, vertex));
    }
    
    protected boolean hasInIO(List<V> io_list, V vertex) {
        return (this.getFromIO(io_list,vertex) != null);
    }
    
    protected void addToIO(List<V> io_list, V vertex) {
        
        this.addVertex(vertex);
        
        if(this.hasInIO(io_list, vertex)) {  
            this.replaceVertex(this.getFromIO(io_list, vertex), vertex);
            return;
        }
        
        io_list.add(vertex);
    }
    
    
    public List<V> getInputs(){
        return this.inputs;
    }
    
    public V getInput(V vertex) {
        return this.getFromIO(this.getOutputs(), vertex);
    }
    
    public boolean hasInput(V vertex) {
        return this.hasInIO(this.getInputs(), vertex);
    }
    
    public void removeInput(V vertex) {
        this.removeFromIO(this.getInputs(), vertex);
    }
    
    public V addInput(V vertex) {

        if(this.hasOutput(vertex)) {
            
            this.addVertex(vertex);
            this.replaceVertex(this.getOutput(vertex), vertex);
        
        }
        
        this.addToIO(this.getInputs(), vertex);
        
        return vertex;
    }
    
    public List<V> getOutputs(){
        return this.outputs;
    }
    
    public V getOutput(V vertex) {
        return this.getFromIO(this.getOutputs(), vertex);
    }
    
    public boolean hasOutput(V vertex) {
        return this.hasInIO(this.getOutputs(), vertex);
    }
    
    public void removeOutput(V vertex) {
        this.removeFromIO(this.getOutputs(), vertex);
    }
    
    public V addOutput(V vertex) {
        this.addToIO(this.getOutputs(), vertex);
        return vertex;
    }
    
    public V getFirstOutput() {
        return this.getOutputs().get(0);
    }

    public V getFirstInput() {
        return this.getInputs().get(0);
    }
    
    public void removeNodeAndJoinEdges(V vertex) {
        
        this.incomingEdgesOf(vertex).forEach((in_edge) -> {
            this.outgoingEdgesOf(vertex).forEach((out_edge) -> {
                this.addEdge(this.getEdgeSource(in_edge), this.getEdgeTarget(out_edge));
            });
        });
  
        this.removeVertex(vertex);
    }
    
    public GeneralFlowGraph<V,E> mergeGraph(GeneralFlowGraph<V,E> graph) {
        
            // Joins the new graph to the current one
           Graphs.addGraph(this, graph);
           
      // Merge this graph output nodes that output to graph input nodes, and add the independent input nodes of graph to this graph's inputs
           for(V vertex : graph.getInputs()) {

              
              if(this.hasOutput(vertex)) {   
    
                  this.replaceVertex(graph.getInput(vertex), this.getOutput(vertex)); 
                  
                  this.removeNodeAndJoinEdges(this.getOutput(vertex));
                  this.outputs.remove(vertex);
      
              }else {
      
                  if(this.hasInput(vertex)) {
                      this.replaceVertex(graph.getInput(vertex), this.getInput(vertex));
                  }else{   
                      this.addInput(vertex);
                  }
              }

          }
          
          // merges the output nodes of both graphs
          
          graph.getOutputs().forEach((vertex) -> {
              
              if(!this.hasOutput(vertex)) {   
                  this.addOutput(vertex);
               }else {
                   this.replaceVertex(graph.getOutput(vertex), this.getOutput(vertex));
               }
              
          });

          
          
           return this;
       }
    
    public V addOperation(V operator, V operand_left, V operand_right) {
        
        this.addVertex(operator);
        this.addVertex(operand_left);
        this.addVertex(operand_right);
        this.addEdge(operand_left, operator);
        this.addEdge(operand_right, operator);
        
        return operator;
    }
    
    public V addOperation(V operator, V operand) {
        
        this.addVertex(operator);
        this.addVertex(operand);
        
        this.addEdge(operand, operator);
        
        return operator;
    }
}
