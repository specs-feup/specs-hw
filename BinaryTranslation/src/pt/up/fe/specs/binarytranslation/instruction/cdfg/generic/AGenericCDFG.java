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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.generic;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.edge.GenericCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.AGenericCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.data.AGenericCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.data.GenericCDFGVariableNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.AGenericCDFGOperationNode;

/**
 * @author João Conceição
 */

public abstract class AGenericCDFG extends DirectedMultigraph<AGenericCDFGNode, GenericCDFGEdge>{

    private Map<String, AGenericCDFGDataNode> inputs;
    private Map<String, AGenericCDFGDataNode> outputs;

    private String unique_node_str;
    private int unique_node_id;
    
    protected AGenericCDFG(String unique_node_str) {
        
        super(GenericCDFGEdge.class);
        
        this.inputs = new HashMap<String, AGenericCDFGDataNode>();
        this.outputs = new HashMap<String, AGenericCDFGDataNode>();
        
        this.unique_node_id = 0;
        this.unique_node_str = unique_node_str;
        
    }
    
// Methods for adding edges
    
    protected GenericCDFGEdge insertEdge(AGenericCDFGNode source, AGenericCDFGNode destination, GenericCDFGEdge edge) {     
        
        this.addEdge(source, destination, edge);
        
        return edge;
    }
    
    
    /** Creates and adds a {@code GenericCDFGDataEdge} without a modifier to the current graph.
     * 
     * @param source Source {@code AGenericCDFGNode} of the edge
     * @param destination Destination {@code AGenericCDFGNode} of the edge
     * @return The created {@code GenericCDFGDataEdge}
     */
    @Override
    public GenericCDFGEdge addEdge(AGenericCDFGNode source, AGenericCDFGNode destination) {     
        
        if(source instanceof AGenericCDFGDataNode) {
            return this.insertEdge(source, destination, new GenericCDFGEdge()); 
        }else if(destination instanceof AGenericCDFGDataNode) {
            return this.insertEdge(source, destination, new GenericCDFGEdge()); 
        }
        
        return this.insertEdge(source, destination, new GenericCDFGEdge()); 
    }
    
    /** Creates and adds a {@code GenericCDFGDataEdge} to the current graph.
     * 
     * @param source Source {@code AGenericCDFGNode} of the edge
     * @param destination Destination {@code AGenericCDFGNode} of the edge
     * @param modifier Modifier function to be applied to this edge (sign-extend(), unsigned(), float(), etc)
     * @return The created {@code GenericCDFGDataEdge}
     */
    public GenericCDFGEdge addEdge(AGenericCDFGNode source, AGenericCDFGNode destination, String modifier) {
        return this.insertEdge(source, destination, new GenericCDFGEdge());
    }
    
    /** Creates and adds a {@code GenericCDFGControlEdge} with a condition to the current graph.
     * 
     * @param source Source node of the edge
     * @param destination Destination node of the edge
     * @param condition Condition that enables the {@code GenericCDFGControlEdge}
     * @return The created {@code GenericCDFGControlEdge}
     */
    public GenericCDFGEdge addEdge(AGenericCDFGNode source, AGenericCDFGNode destination, boolean condition) {   
        
        GenericCDFGEdge edge = new GenericCDFGEdge();
        edge.setCondition(condition);
        
        return this.insertEdge(source, destination, edge);
    }
    
    
    //garbage
    public GenericCDFGEdge outgoingEdge(AGenericCDFGNode node) {
        
        for(GenericCDFGEdge edge : this.outgoingEdgesOf(node)) {
            if(edge.getCondition() == "") {
                return edge;
            }
        }
        
        return null;
    }
  //garbage
    public GenericCDFGEdge incomingLeftEdge(AGenericCDFGNode node) {
        
        for(GenericCDFGEdge edge : this.incomingEdgesOf(node)) {
            if(edge.getCondition() == "") {
                return edge;
            }
        }
        
        return null;
    }
    
  
  //garbage
    public GenericCDFGEdge incomingRightEdge(AGenericCDFGNode node) {
        
        for(GenericCDFGEdge edge : this.incomingEdgesOf(node)) {
            if((edge.getCondition() == "") && (!this.incomingLeftEdge(node).equals(edge))) {
                return edge;
            }
        }
        
        return null;
    }
    
    /** Adds a {@code AGenericCDFGNode} to the graph and returns it
     * 
     * @param node {@code AGenericCDFGNode} to be added to the graph
     * @return {@code AGenericCDFGNode} added to the graph
     */
    public AGenericCDFGNode addNode(AGenericCDFGNode node){
        
        if(!this.containsVertex(node)) {
            if(!(node instanceof AGenericCDFGDataNode)) {
                node.prependToName(unique_node_str + String.valueOf(this.unique_node_id++));
            }
           
            this.addVertex(node);
        }

        return node;
    }
    
    /** Adds an input {@code AGenericCDFGDataNode} to the inputs map of the current graph and returns it, if a node with the same name does not exist. 
     * If a node with the same node exists, the previous node is returned.<br>
     * This method does not add the node to the graph, so {@link #addNode(AGenericCDFGNode)} with the node returned from this method as 
     * argument should be called in order to actually add the node to the graph.
     * 
     * 
     * @param node {@code AGenericCDFGDataNode} to be added
     * @return The {@code AGenericCDFGDataNode} passed as argument if a node with the same name is not present in the input map, and if present, the node in the map
     */
    public AGenericCDFGNode addInputNode(AGenericCDFGNode node) {    
        
        if(node instanceof GenericCDFGVariableNode) {
            
            AGenericCDFGDataNode data_node = (AGenericCDFGDataNode)node;
            
            if(this.inputs.containsKey(data_node.toString())) {
                return this.inputs.get(data_node.toString());
            }else {
                this.inputs.put(data_node.toString(), data_node);
                return node;
            } 
        }
        return node;  
    }
    

    /** Get a {@code Map<String, AGenericCDFGDataNode> } of the inputs of the current graph
     *  
     * @return A  {@code Map<String, AGenericCDFGDataNode>} of the inputs of the current graph
     */
    public Map<String, AGenericCDFGDataNode> getInputs(){    
        return this.inputs;
    }
    
    /** Adds an input {@code AGenericCDFGDataNode} to the outputs map of the current graph and returns it, if a node with the same name does not exist. 
     * If a node with the same node exists, the previous node is returned.<br>
     * This method does not add the node to the graph, so {@link #addNode(AGenericCDFGNode)} with the node returned from this method as 
     * argument should be called in order to actually add the node to the graph.
     * 
     * 
     * @param node {@code AGenericCDFGDataNode} to be added
     * @return The {@code AGenericCDFGDataNode} passed as argument if a node with the same name is not present in the outputs map, and if present, the node in the map
     */
    public AGenericCDFGNode addOutputNode(AGenericCDFGNode node) {

        if(node instanceof GenericCDFGVariableNode) {
            
            AGenericCDFGDataNode data_node = (AGenericCDFGDataNode)node;
            
            if(this.outputs.containsKey(data_node.toString())) {
                return this.outputs.get(data_node.toString());
            }else {
                this.outputs.put(data_node.toString(), data_node);
                return node;
            } 
        }
        
        return node;
    }
    

    /** Get a {@code Map<String, AGenericCDFGDataNode> } of the outputs of the current graph
     *  
     * @return A  {@code Map<String, AGenericCDFGDataNode>} of the outputs of the current graph
     */
    public Map<String, AGenericCDFGDataNode>  getOutputs(){
        return this.outputs;
    }
    
    public void replaceNode(AGenericCDFGNode old_node, AGenericCDFGNode new_node) {
        
        for(GenericCDFGEdge in_edge : this.incomingEdgesOf(old_node)) {
            this.insertEdge(this.getEdgeSource(in_edge), new_node, in_edge.duplicate());
        }
        
        for(GenericCDFGEdge out_edge : this.outgoingEdgesOf(old_node)) {
            this.insertEdge(new_node, this.getEdgeTarget(out_edge), out_edge.duplicate());
        }
        
        this.removeVertex(old_node);
        
        if((old_node instanceof AGenericCDFGDataNode) && (new_node instanceof AGenericCDFGDataNode)) {
            
            if(this.getInputs().containsValue(old_node)) {
                this.getInputs().put(old_node.getName(), (AGenericCDFGDataNode)new_node);
            }
            if(this.getOutputs().containsValue(old_node)) {
                this.getOutputs().put(old_node.getName(), (AGenericCDFGDataNode)new_node);
            }
        }
       
    }

    
    public void removeNodeAndJoinEdges(AGenericCDFGNode node) {
        
        for(var in_edge : this.incomingEdgesOf(node)) {
            for(var out_edge : this.outgoingEdgesOf(node)) {
                this.addEdge(this.getEdgeSource(in_edge), this.getEdgeTarget(out_edge));
            }
        }
        
        this.removeVertex(node);
        
    }

    /** Merges the current graph with the graph passed as argument, and the resulting graph replaces the current graph <br>
     * The resulting graph will have as outputs the nodes of the current graph that do not merge with the graph 
     * passed as argument, and the inputs will be the current graph's input nodes with the input nodes of the graph
     *  passed as argument that were not merged 
     * @param graph Graph to merge with the current graph
     * @return A reference to the graph resulting from the merge
     */
    public AGenericCDFG mergeGraph(AGenericCDFG graph) {
        
     // Joins the new graph to the current one
        Graphs.addGraph(this, graph);
        
        
       // Merge this graph output nodes that output to graph input nodes, and add the independent input nodes of graph to this graph's inputs
       for(String node_name : graph.getInputs().keySet()) {

           if(this.getOutputs().containsKey(node_name)) {   

               this.replaceNode(graph.getInputs().get(node_name), this.getOutputs().get(node_name)); 
               
               this.removeNodeAndJoinEdges(this.outputs.get(node_name));
               this.outputs.remove(node_name);
   
           }else {
   
               if(this.getInputs().containsKey(node_name)) {
                   this.replaceNode(graph.getInputs().get(node_name), this.getInputs().get(node_name));
               }else{   
                   this.inputs.put(node_name, graph.getInputs().get(node_name));
               }
           }

       }
       
       // merges the output nodes of both graphs
       
       for(String node_name : graph.getOutputs().keySet()) {

           if(!this.getOutputs().containsKey(node_name)) {   
               
              this.outputs.put(node_name, graph.getOutputs().get(node_name));
   
           }else {
               this.replaceNode(graph.getOutputs().get(node_name), this.getOutputs().get(node_name));
           }
       }

        return this;
    }
    
    //Method for printing graphs
    
    /** Generates a DOT graph of the current {@code InstructionCDFG}  <br><br>
     * 
     * Graphical representation: <br>
     * 
     * 
     * <dl>
     * <dt><b>Nodes</b></dt>
     * <dd><b>Operations:</b> boxes</dd>
     * <dd><b>Operands:</b> circles</dd>
     * <dt><b>Edges</b></dt>
     * <dd><b>Dataflow:</b> lines w/ modifiers if applicable</dd>
     * <dd><b>Controlflow:</b> arrows w/ condition</dd>
     * </dl>
     * 
     * @param title Title of the DOT graph to be generated
     */
    public void toDot(String title) {
       
        DOTExporter<AGenericCDFGNode, GenericCDFGEdge> exporter = new DOTExporter<>();
    
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            
            if(v instanceof AGenericCDFGOperationNode) {
                map.put("label", DefaultAttribute.createAttribute(v.toString() + 
                        "\\n" +   v.getName() + 
                        "\\n(" +   String.valueOf(((AGenericCDFGOperationNode)v).operatorPrecedence()) + 
                        ")" 
                        ));  
            }else {
                map.put("label", DefaultAttribute.createAttribute(v.toString()));  
            }
            
                
            map.put("shape", DefaultAttribute.createAttribute(v.getDotShape()));

            return map;
        });
        
        exporter.setEdgeAttributeProvider((e) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            
     
            map.put("label", DefaultAttribute.createAttribute(String.valueOf(e.getCondition()))); 
            map.put("arrowhead", DefaultAttribute.createAttribute(e.getDotShape()));
        
            return map;
        });
        
        Writer writer = new StringWriter();
        exporter.exportGraph(this, writer);
        System.out.flush();
        System.out.println(writer.toString());
        System.out.flush();
    }
    
    public AGenericCDFG paralelizeOperations() {
        return null;
    }
    
    public AGenericCDFG simplifyOperations() {
        return null;
    }
}