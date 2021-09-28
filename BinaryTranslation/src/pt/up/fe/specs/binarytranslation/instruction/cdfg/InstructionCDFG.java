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

package pt.up.fe.specs.binarytranslation.instruction.cdfg;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.edges.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.edges.InstructionCDFGControlEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.edges.InstructionCDFGDataEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.data.InstructionCDFGRegister;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.AInstructionCDFGExpressionNode;

/*
 * Maybe the nodes can represent operations and data and the edges represent control, so conditions can be inserted into the edges ?
 * 
 * In the CDFGs that I've researched the control and dataflow are represented as edges, and only the operations are represented as nodes
 *      But Jgrapht doesn't really allow the creation of edges without explicit source and target nodes, so the method implemented
 *          in the InstructionCDFGGenerator doesn't work if data is represented as edges (and maybe with control transfers it doesn't as well)
 *              Maybe create classes that allow for the creation of pseudo-edges that only have a source or a target ?
 * The functions that change data (sext, signed, msb, etc) amd range subscripts should maybe be modifiers to the edges (in case that the data is a node and not an edge)
 *  This is because this is more similar to how it would be used in Verilog (unsigned wire, signed wire)
 *      Maybe not all though
 */

public class InstructionCDFG extends DirectedMultigraph<AInstructionCDFGNode, AInstructionCDFGEdge>{

    public InstructionCDFG() {
        super(AInstructionCDFGEdge.class);
        
    }
    
    public InstructionCDFG(AInstructionCDFGNode node) {
        this();
        this.addVertex(node);
    }
    

    @Override 
    public AInstructionCDFGEdge addEdge(AInstructionCDFGNode source, AInstructionCDFGNode destination) {
        
        AInstructionCDFGEdge edge = new InstructionCDFGDataEdge();
        
        this.addEdge(source, destination, edge);
        
        return edge;
    }
    
    public AInstructionCDFGEdge addEdge(AInstructionCDFGNode source, AInstructionCDFGNode destination, String modifier) {
        
        AInstructionCDFGEdge edge = new InstructionCDFGDataEdge(modifier);
        
        this.addEdge(source, destination, edge);
        
        return edge;
    }
    
    public AInstructionCDFGEdge addEdge(AInstructionCDFGNode source, AInstructionCDFGNode destination, boolean condition) {
        
        AInstructionCDFGEdge edge = new InstructionCDFGControlEdge(condition);
        
        this.addEdge(source, destination, edge);
        
        return edge;
    }
    
    /** Adds a node to the graph and returns it
     * 
     * @param node node to be added to the graph
     * @return node added to the graph
     */
    public AInstructionCDFGNode addNode(AInstructionCDFGNode node){
        
        this.addVertex(node);
        
        return node;
    }
    public InstructionCDFG mergeGraph(InstructionCDFG graph) {
         Graphs.addGraph(this, graph);
         return this;
    }
    
    public InstructionCDFG mergeInputNodes() {
        
        Map<String, List<InstructionCDFGRegister>> inputs = new HashMap<String, List<InstructionCDFGRegister>>();
        
        for(var node : this.vertexSet()) {
            
          if((node instanceof InstructionCDFGRegister) && (this.incomingEdgesOf(node).isEmpty())) {
                
                if(!inputs.containsKey(node.toString())) {
                    inputs.put(node.toString(), new ArrayList<InstructionCDFGRegister>());
                }
                inputs.get(node.toString()).add((InstructionCDFGRegister)node);
            }
        }
        
        
        for(var node : inputs.values()) {
            
            InstructionCDFGRegister main_node = node.get(0);
            
            for(var node_red : node) {
                
                if(!node_red.equals(main_node)) {
                    
                    for(var a : this.edgesOf(node_red)) {
                        
                        this.addEdge(main_node, this.getEdgeTarget(a));
                        this.removeEdge(a);
                    }
                    this.removeVertex(node_red);
                }
            }
            
        }
        
        return this;
    }
    
    public InstructionCDFG mergeOutputNodes() {
        
        Map<String, List<InstructionCDFGRegister>> outputs = new HashMap<String, List<InstructionCDFGRegister>>();
        
        for(var node : this.vertexSet()) {
            
          if((node instanceof InstructionCDFGRegister) && (this.outgoingEdgesOf(node).isEmpty())) {
                
                if(!outputs.containsKey(node.toString())) {
                    outputs.put(node.toString(), new ArrayList<InstructionCDFGRegister>());
                }
                outputs.get(node.toString()).add((InstructionCDFGRegister)node);
            }
        }
        
        
        for(var node : outputs.values()) {
            
            InstructionCDFGRegister main_node = node.get(0);
            
            for(var node_red : node) {
                
                if(!node_red.equals(main_node)) {
                    
                    for(var a : this.edgesOf(node_red)) {
                        
                        this.addEdge(this.getEdgeSource(a),main_node);
                        this.removeEdge(a);
                    }
                    this.removeVertex(node_red);
                }
            }
            
        }
        
        return this;
    }
    

    
    /*
     * Create generic for allowing the printing of all types of graphs
     * 
     */
    
    //Method for printing graphs
    
    public void toDot(String title) {
       
        DOTExporter<AInstructionCDFGNode, AInstructionCDFGEdge> exporter = new DOTExporter<>();
        
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v.toString()));
            
            map.put("shape", DefaultAttribute.createAttribute((v instanceof AInstructionCDFGExpressionNode) ? "box" : "circle"));

            return map;
        });
        
        exporter.setEdgeAttributeProvider((e) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(e.toString()));
            

            map.put("arrowhead", DefaultAttribute.createAttribute((e instanceof InstructionCDFGDataEdge) ? "none" : "normal"));
        
            return map;
        });
        
        Writer writer = new StringWriter();
        exporter.exportGraph(this, writer);
        System.out.println(writer.toString());
        
    }
  
}
