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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.dataflowgraph.DataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.operand.InstructionCDFGLeftOperandEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.operand.InstructionCDFGRightOperandEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.operand.InstructionCDFGUnaryOperandEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.control.AInstructionCDFGControlNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.AInstructionCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGLiteralNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGVariableNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.AInstructionCDFGOperationNode;

public abstract class AInstructionCDFGSubgraph extends DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>{

    private Map<String, Integer> UIDMap;
    
    private int subgraph_uid;
    static private int subgraph_uid_counter = 0;

    
    public AInstructionCDFGSubgraph() {
        super(AInstructionCDFGDataNode.class, AInstructionCDFGOperationNode.class, AInstructionCDFGEdge.class);
        this.UIDMap = new HashMap<>();
        this.subgraph_uid = (AInstructionCDFGSubgraph.subgraph_uid_counter++);
    }
    
    /** Generates the inputs and the outputs of the graph*/
    public void generateIO() {
        
        this.generateInputs();
        this.generateOutputs();
        
    }
    
    @Override
    public int hashCode() {
        return subgraph_uid;
    }
    
    /** Generates the inputs and the outputs of the graph and sets the input UIDs and generates the outputs UIDs
     * 
     * @param seedUIDMap UIDMap of the graph inputs
     * @return The graph itself
     */
    public AInstructionCDFGSubgraph resolveAll(Map<String, Integer> seedUIDMap) {
        
        this.generateIO();
        this.resolveUIDMaps(seedUIDMap);
        
        return this;
    }
    
    private Map<String, Integer> generateUIDMapFromVertices(Collection<AInstructionCDFGNode> vertices){
        
        Map<String, Integer> generatedUIDMap = new HashMap<>();
        
        vertices.forEach(vertex -> {
            if(!(vertex instanceof AInstructionCDFGControlNode) && !(vertex instanceof InstructionCDFGLiteralNode)) {
                generatedUIDMap.put(vertex.getReference(), vertex.getUIDVal());
            }
        });
        
        return generatedUIDMap;
    }
    
    private Map<String, Integer> generateUIDMap(Collection<AInstructionCDFGNode> vertices, Map<String, Integer> UIDMap, int increment){
        
        Map<String, Integer> generatedUIDMap = new HashMap<>();
        
        vertices.forEach(vertex -> {
            if(!(vertex instanceof AInstructionCDFGControlNode) && !(vertex instanceof InstructionCDFGLiteralNode)) {
                generatedUIDMap.put(vertex.getReference(), vertex.setUID(UIDMap.containsKey(vertex.getReference()) ? UIDMap.get(vertex.getReference()) + increment : 0));
            }
        });
        
        return generatedUIDMap;
    }
    
    private Map<String, Integer> setUIDMap(Collection<AInstructionCDFGNode> vertices, Map<String, Integer> UIDMap) {
        
        vertices.stream().filter(vertex -> UIDMap.containsKey(vertex.getReference())).forEach(vertex -> vertex.setUID(UIDMap.get(vertex.getReference())));
        
        return UIDMap;
    }
    
    public void resolveUIDMaps(Map<String, Integer> seedUIDMap) {
        
        this.setInputUIDMap(seedUIDMap);
        this.updateInternalUIDMap(seedUIDMap);
        this.generateOutputUIDMap();
        
    }    
    
    private Map<String, Integer> updateInternalUIDMap(Map<String, Integer> UIDMap) {
        UIDMap.forEach((reference, uid) -> {
            if(this.UIDMap.putIfAbsent(reference, uid) != null) {
                if(this.UIDMap.get(reference).intValue() < UIDMap.get(reference).intValue()) {
                    this.UIDMap.replace(reference, uid);
                }
            }
            
        });
        
        return UIDMap;
    }
    
    public Map<String, Integer> getCurrentUIDMap(){
        return this.UIDMap;
    }
    
    public Map<String, Integer> generateInputUIDMap(){
        return this.updateInternalUIDMap(this.generateUIDMapFromVertices(this.getInputs()));
    }
    
    public void setInputUIDMap(Map<String, Integer> inputUIDMap) {
        this.updateInternalUIDMap(this.setUIDMap(this.getInputs(), inputUIDMap));
    }
    
    public Map<String, Integer> getInputUIDMap(){
        return this.generateUIDMapFromVertices(this.getInputs());
    }
    
    public Map<String, Integer> generateOutputUIDMap() {
        return this.updateInternalUIDMap(this.generateUIDMap(this.getOutputs(), this.getCurrentUIDMap() , 1));
    }
    
    public void setOutputUIDMap(Map<String, Integer> outputUIDMap) {
        this.updateInternalUIDMap(this.setUIDMap(this.getOutputs(), outputUIDMap));
    }
    
    public Map<String, Integer> getOutputUIDMap(){
        return this.generateUIDMapFromVertices(this.getOutputs());
    }

    @Override
    public void generateOutputs() {
        this.setOutputs(this.getVertexStreamOfPredicate(v -> v instanceof AInstructionCDFGDataNode).filter(v -> !this.incomingEdgesOf(v).isEmpty()).collect(Collectors.toSet()));
    }
    
    public void setVertexUID(AInstructionCDFGNode vertex) {
        vertex.setUID(this.UIDMap.containsKey(vertex.getReference()) ? this.UIDMap.get(vertex.getReference()) + 1 : 0);
    }
    
    @Override
    public void suppressVertex(AInstructionCDFGNode vertex) {
        
        this.incomingEdgesOf(vertex).forEach(in_edge -> {
            this.outgoingEdgesOf(vertex).forEach(out_edge -> {
                this.addEdge(this.getEdgeSource(in_edge), this.getEdgeTarget(out_edge), out_edge.duplicate());
            });
        });
  
        this.removeVertex(vertex);
    }
    
    @Override
    public boolean addVertex(AInstructionCDFGNode vertex) throws IllegalArgumentException {
        
        if(!this.containsVertex(vertex)) {
            this.setVertexUID(vertex);
            return super.addVertex(vertex);
        }else {
            return false;
        }
    }
    
    @Override
    public void addVertices(Set<AInstructionCDFGNode> vertices) {
        vertices.forEach(v -> this.addVertex(v));
    }
    
    @Override
    public void mergeVertices(AInstructionCDFGNode vertex, Collection<AInstructionCDFGNode> vertices) throws IllegalArgumentException {        
        vertices.forEach(v -> this.replaceVertex(v, vertex));
    }
    
    /** Adds an unary operation to the graph
     * 
     * @param operator Operator node of the operation
     * @param operand Operand of the operation
     * @return Operator node
     */
    public AInstructionCDFGNode addOperation(AInstructionCDFGNode operator, AInstructionCDFGNode operand) {
        
        this.addVertices(Set.of(operator, operand));
        
        this.addEdge(operand, operator, (operand instanceof InstructionCDFGVariableNode) ? new InstructionCDFGUnaryOperandEdge(((InstructionCDFGVariableNode)operand).getModifiers()) : new InstructionCDFGUnaryOperandEdge());
        
        return operator;
    }
    
    /** Adds a binary operation to the graph (with order of operands)
     * 
     * @param operator Operator node of the operation
     * @param operandLeft Left operand node of the operation
     * @param operandRight Right operand node of the operation
     * @return Operator node
     */
    public AInstructionCDFGNode addOperation(AInstructionCDFGNode operator, AInstructionCDFGNode operandLeft, AInstructionCDFGNode operandRight) {

        this.addVertices(Set.of(operator, operandLeft, operandRight));
        
        this.addEdge(operandLeft, operator, (operandLeft instanceof InstructionCDFGVariableNode) ? new InstructionCDFGLeftOperandEdge(((InstructionCDFGVariableNode)operandLeft).getModifiers()) : new InstructionCDFGLeftOperandEdge());
        this.addEdge(operandRight, operator, (operandRight instanceof InstructionCDFGVariableNode) ? new InstructionCDFGRightOperandEdge(((InstructionCDFGVariableNode)operandRight).getModifiers()) : new InstructionCDFGRightOperandEdge());

        return operator;
    }
    
    public AInstructionCDFGNode getLeftOperand(AInstructionCDFGNode operator) {
        return this.getOperand(operator, InstructionCDFGLeftOperandEdge.class);
    }
    
    public InstructionCDFGLeftOperandEdge getLeftOperandEdge(AInstructionCDFGNode operator) {
        return (InstructionCDFGLeftOperandEdge) this.getEdge(this.getLeftOperand(operator), operator);
    }
    
    public AInstructionCDFGNode getRightOperand(AInstructionCDFGNode operator) {
        return this.getOperand(operator, InstructionCDFGRightOperandEdge.class);
    }
    
    public InstructionCDFGRightOperandEdge getRightOperandEdge(AInstructionCDFGNode operator) {
        return (InstructionCDFGRightOperandEdge) this.getEdge(this.getRightOperand(operator), operator);
    }
    
    public void copyVerticesTo(AInstructionCDFGSubgraph destinationSubgraph) {
        
        Set<AInstructionCDFGNode> currentVertices = new LinkedHashSet<>();
        Set<AInstructionCDFGNode> nextVertices = new LinkedHashSet<>();
        
        currentVertices.addAll(this.getInputs());
        
        while(!currentVertices.isEmpty()) {
            
            nextVertices.addAll(this.getVerticesAfter(currentVertices));
            
            currentVertices.forEach(vertex -> {
                destinationSubgraph.addVertex(vertex);
                this.getVerticesBefore(vertex).forEach(before -> {
                    destinationSubgraph.addEdge(before, vertex, this.getEdge(before, vertex).duplicate());
                });
            });
            
            
            currentVertices.clear();
            currentVertices.addAll(nextVertices);
            nextVertices.clear();
        }
        
        destinationSubgraph.generateInputs();
        destinationSubgraph.generateOutputs();
        
    }
}