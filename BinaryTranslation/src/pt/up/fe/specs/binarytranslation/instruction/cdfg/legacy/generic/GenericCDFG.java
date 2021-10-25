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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.edge.AGenericCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.edge.GenericCDFGControlEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.AGenericCDFGNode;
import pt.up.fe.specs.util.SpecsLogs;

public class GenericCDFG<Node extends AGenericCDFGNode, Edge extends AGenericCDFGEdge, DataEdge extends Edge, ControlEdge extends Edge, DataNode extends Node, OperationNode extends Node>  extends DirectedMultigraph<Node,Edge>{

    //private final Class<? extends Node> NodeClass;
    private final Class<? extends Edge> EdgeClass;
    
    private final Class<? extends DataEdge> DataEdgeClass;
    private final Class<? extends ControlEdge> ControlEdgeClass;
    
    private final Class<? extends DataNode> DataNodeClass;
    private final Class<? extends OperationNode> OperationNodeClass;
    
    private Map<String, DataNode> inputs;
    private Map<String, DataNode> outputs;
    
    private final String node_uid_str;
    private int node_uid_n;
    
    @SuppressWarnings("unchecked")
    public GenericCDFG(String node_uid, Class<? extends Edge> EdgeClass, Class<? extends DataEdge> DataEdgeClass, Class<? extends ControlEdge> ControlEdgeClass, Class<? extends DataNode> DataNodeClass, Class<? extends OperationNode> OperationNodeClass) {
        super(EdgeClass);
        
        this.inputs = new HashMap<String, DataNode>();
        this.outputs = new HashMap<String, DataNode>();
        
        this.EdgeClass = EdgeClass;
        
        this.DataEdgeClass = DataEdgeClass;
        this.ControlEdgeClass = ControlEdgeClass;
        
        this.DataNodeClass = DataNodeClass;
        this.OperationNodeClass = OperationNodeClass;
        
        this.node_uid_str = node_uid;
        this.node_uid_n = 0;
    }
    
    public String generateNodeUID() {
        return this.node_uid_str + String.valueOf(this.node_uid_n++);
    }
    
    public Edge insertEdge(Node source, Node destination, Edge edge) {
        
        this.addEdge(source, destination, edge);
        
        return edge;
    }
    /*
    @SuppressWarnings("deprecation")
    public Edge insertEdge(Node source, Node destination, Class<? extends Edge> edge, Class<?> contructor_class, Object ... constructor_parameters) {

        try {
            return this.insertEdge(source, destination, edge.getConstructor(contructor_class).newInstance(constructor_parameters));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
           // SpecsLogs.msgWarn("Error message:\n", e);
            return null;
        }
  
    }
    */
    @Override
    public Edge addEdge(Node source, Node destination) {
       // return this.insertEdge(source, destination, DataEdgeClass, null);
        
        try {
            return this.insertEdge(source, destination, DataEdgeClass.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
           // SpecsLogs.msgWarn("Error message:\n", e);
            return null;
        }
        
    }
    
    public Edge addEdge(Node source, Node destination, Boolean condition) {   
        //return this.insertEdge(source, destination,ControlEdgeClass, Boolean.class, new Object[] {condition});
        
        try {
            return this.insertEdge(source, destination, ControlEdgeClass.getConstructor( Boolean.class).newInstance(new Object[] {condition}));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
           // SpecsLogs.msgWarn("Error message:\n", e);
            return null;
        }
    }
    
    public List<Edge> incomingDataEdgesOf(Node node){
        return this.getEdgesOfClassOf(this.incomingEdgesOf(node), DataEdgeClass);
     }
    
    public List<Edge> outgoingDataEdgesOf(Node node){
        return this.getEdgesOfClassOf(this.outgoingEdgesOf(node), DataEdgeClass);
    }
    
    public List<Edge> incomingControlEdgesOf(Node node){
        return this.getEdgesOfClassOf(this.incomingEdgesOf(node), ControlEdgeClass);
     }
    
    private List<ControlEdge> outgoingConditionControlEdgesOf(Node node, String condition){
        List<ControlEdge> edges = new ArrayList<ControlEdge>();
        
        for(AGenericCDFGEdge edge : this.outgoingControlEdgesOf(node)) {
            if(((GenericCDFGControlEdge)edge).getCondition().equals(condition)) {
                edges.add((ControlEdge) edge);
            }
        }
        return edges;
    }
    
    public List<ControlEdge> outgoingFalseControlEdgesOf(Node node){
        return this.outgoingConditionControlEdgesOf(node, String.valueOf(false));
    }
    
    public List<ControlEdge> outgoingTrueControlEdgesOf(Node node){
        return this.outgoingConditionControlEdgesOf(node, String.valueOf(true));
    }
    
    public List<Edge> outgoingControlEdgesOf(Node node){
        return this.getEdgesOfClassOf(this.outgoingEdgesOf(node), ControlEdgeClass);
    }
    
    private List<Edge> getEdgesOfClassOf(Set<Edge> edge_set, Class<?> edge_class){
        
        List<Edge> edges = new ArrayList<Edge>();
        
        for(Edge edge : edge_set) {
 
            if(edge_class.isInstance(edge)) {
                edges.add(edge);
            }
        }
        
        return edges;       
    }
    
    
    public Node addNode(Node node){
        
        if(!this.containsVertex(node)) {
            if(!(DataNodeClass.isAssignableFrom(node.getClass()))) {
                node.prependToName(this.generateNodeUID());
            }
           
            this.addVertex(node);
        }

        return node;
    }
    
    public Node addIONode(Node node, Map<String, DataNode> io_map) {
        
        if(DataNodeClass.isAssignableFrom(node.getClass())) {

            DataNode data_node = DataNodeClass.cast(node);
            
            if(io_map.containsKey(data_node.toString())) {
                return io_map.get(data_node.toString());
            }else {
                this.addNode(node);
                io_map.put(data_node.toString(), data_node);
                return node;
            } 
        }
        return node; 
    }
    
    public Node addInputNode(Node node) {    
        return this.addIONode(node, this.getInputs());
    }
    
    public Map<String, DataNode> getInputs(){
        return this.inputs;
    }
    
    public List<DataNode> getInputList(){
        return new ArrayList<DataNode>(this.getInputs().values());
    }
    
    public Node addOutputNode(Node node) {
        return this.addIONode(node, this.getOutputs());
    }
    
    public Map<String, DataNode> getOutputs(){
        return this.outputs;
    }
    
    public List<DataNode> getOutputList(){
        return new ArrayList<DataNode>(this.getOutputs().values());
    }
    
    public Set<Node> getOperationNodes(){
        
        Set<Node> operation_nodes = new LinkedHashSet<Node>();
        
        
        for(Node node : this.vertexSet()) {
            
            if((!this.getOutputList().contains(node)) && (!this.getInputList().contains(node))) {
                operation_nodes.add(node);
            }
            
        }
        
        return operation_nodes;
    }
    
    public Node getNodeWithSameUID(String node_uid) {
        
        for(Node node: this.vertexSet()) {
            if(node.getName().equals(node_uid)) {
                return node;
            }
        }
        return null;
    }
    
    public Node getNodeWithSameUID(Node node) {
        return this.getNodeWithSameUID(node.getName());
    }
    
    public Node replaceNode(Node old_node, Node new_node) {
  
        for(Edge in_edge : this.incomingEdgesOf(old_node)) {       
            this.insertEdge(this.getEdgeSource(in_edge), new_node, EdgeClass.cast(in_edge.duplicate()));
        }
        
        for(Edge out_edge : this.outgoingEdgesOf(old_node)) {
            this.insertEdge(new_node, this.getEdgeTarget(out_edge), EdgeClass.cast(out_edge.duplicate()));
        }
        
        this.removeVertex(old_node);
        
        if((DataNodeClass.isAssignableFrom(old_node.getClass())) && (DataNodeClass.isAssignableFrom(new_node.getClass()))) {
            
            if(this.getInputs().containsValue(old_node)) {
                this.getInputs().put(old_node.getName(), DataNodeClass.cast(new_node));
            }
            if(this.getOutputs().containsValue(old_node)) {
                this.getOutputs().put(old_node.getName(), DataNodeClass.cast(old_node));
            }
        }
        
        return new_node;  
    }
    
    public void removeNodeAndJoinEdges(Node node) {
        
        for(var in_edge : this.incomingEdgesOf(node)) {
            for(var out_edge : this.outgoingEdgesOf(node)) {
                this.addEdge(this.getEdgeSource(in_edge), this.getEdgeTarget(out_edge));
            }
        }
        
        this.removeVertex(node);
        
    }
    
    public Set<Node> getNodesAfter(Node node){
        
        Set<Node> expressions = new HashSet<Node>();
        
        for(Edge edge : this.outgoingEdgesOf(node)) {
            if(!expressions.contains(this.getEdgeTarget(edge))) {
                expressions.add(this.getEdgeTarget(edge));
            }
        }
        
        return expressions;
    }
    
    public Set<Node> getNodesAfter(List<? extends Node> nodes){
        
        Set<Node> expressions = new HashSet<Node>();
        
        for(Node node : nodes) {
            expressions.addAll(this.getNodesAfter(node));
        }
        return expressions;
    }
    
    public Set<Node> getNodesBefore(Node node){
        
        Set<Node> expressions = new HashSet<Node>();
        
        for(Edge edge : this.incomingEdgesOf(node)) {
                expressions.add(this.getEdgeSource(edge));       
        }
        
        return expressions;
    }
    
    public Set<Node> getNodesBefore(List<? extends Node> nodes){
        
        Set<Node> expressions = new HashSet<Node>();
        
        for(Node node : nodes) {
            expressions.addAll(this.getNodesBefore(node));
        }
        return expressions;
    }
    
    public GenericCDFG<Node, Edge, DataEdge, ControlEdge, DataNode, OperationNode> mergeGraph(GenericCDFG<Node, Edge, DataEdge, ControlEdge, DataNode, OperationNode> graph) {
        
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
    
    public void toDot(String title) {
        
        DOTExporter<Node, Edge> exporter = new DOTExporter<>();
    
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            
            if(OperationNodeClass.isAssignableFrom(v.getClass())) {
                map.put("label", DefaultAttribute.createAttribute(v.toString() + 
                        "\\n" +   v.getName()
                        ));  
            }else {
                map.put("label", DefaultAttribute.createAttribute(v.toString()));  
            }
            
                
            map.put("shape", DefaultAttribute.createAttribute(v.getDotShape()));

            return map;
        });
        
        exporter.setEdgeAttributeProvider((e) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            
     
            map.put("label", e.getDotLabel()); 
            map.put("arrowhead", e.getDotArrowhead());
        
            return map;
        });
        
        Writer writer = new StringWriter();
        exporter.exportGraph(this, writer);
        System.out.flush();
        System.out.println(writer.toString());
        System.out.flush();
    }
    
}
