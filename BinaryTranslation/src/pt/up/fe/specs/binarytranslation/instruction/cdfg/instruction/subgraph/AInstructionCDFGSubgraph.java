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
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.AInstructionCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGVariableNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.AInstructionCDFGOperationNode;

public abstract class AInstructionCDFGSubgraph extends DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>{

    private Map<String, Integer> UIDMap;
    
    public AInstructionCDFGSubgraph() {
        super(AInstructionCDFGDataNode.class, AInstructionCDFGOperationNode.class, AInstructionCDFGEdge.class);
        
        this.UIDMap = new HashMap<>();
    }
 
    private Map<String, Integer> generateUIDMap(Set<AInstructionCDFGNode> vertexSet) {
        
        Map<String,Integer> uidMap = new HashMap<>();
        
        vertexSet.forEach(vertex -> uidMap.put(vertex.getReference(), Integer.valueOf(vertex.getUIDVal())));
        
        return uidMap;
    }
    
    private void setUIDMap(Set<AInstructionCDFGNode> vertexSet, Map<String, Integer> uidMap, int increment) {
        
        vertexSet.forEach(vertex -> {
            if(uidMap.containsKey(vertex.getReference())) {
                vertex.setUID(uidMap.get(vertex.getReference()).intValue() + increment);
            }else {
                this.UIDMap.put(vertex.getReference(),uidMap.get(vertex.getReference()));
            }
        });
        
    }
    
    private void updateInternalUIDMap(Map<String, Integer> UIDMap) {
        UIDMap.forEach((reference, uid) -> {
            if(this.UIDMap.putIfAbsent(reference, uid) != null) {
                if(this.UIDMap.get(reference).intValue() < UIDMap.get(reference).intValue()) {
                    this.UIDMap.replace(reference, uid);
                }
            }
            
        });
    }
    
    public Map<String, Integer> getCurrentUIDMap(){
        return this.UIDMap;
    }
    
    public void setInputUIDMap(Map<String, Integer> inputUIDMap) {
        
        //this.setUIDMap(this.getInputs(), this.generateUIDMap(this.getInputs()), 0);
        
        this.setUIDMap(this.getInputs(), inputUIDMap, 0);
        
        this.UIDMap = inputUIDMap;
    }
    
    public Map<String, Integer> getInputUIDMap(){
        return this.generateUIDMap(this.getInputs());
    }
    
    public Map<String, Integer> generateOutputUIDMap(Map<String, Integer> UIDMap) {
        
        this.setUIDMap(this.getOutputs(), UIDMap, 1);
        this.updateInternalUIDMap(this.getOutputUIDMap());
        return this.getOutputUIDMap();
    }
    
    public void setOutputUIDMap(Map<String, Integer> outputUIDMap) {
        this.setUIDMap(this.getOutputs(), outputUIDMap, 0);
        this.updateInternalUIDMap(outputUIDMap);
    }
    
    public Map<String, Integer> getOutputUIDMap(){
        return this.generateUIDMap(this.getOutputs());
    }

    
   /* @Override
    public void generateInputs() {
        this.setInputs(this.getVertexStreamOfPredicate(v -> this.incomingEdgesOf(v).isEmpty()).filter(v -> v instanceof InstructionCDFGVariableNode).collect(Collectors.toSet()));
    }
    */
    @Override
    public void generateOutputs() {
        this.setOutputs(this.getVertexStreamOfPredicate(v -> v instanceof AInstructionCDFGDataNode).filter(v -> !this.incomingEdgesOf(v).isEmpty()).collect(Collectors.toSet()));
    }
    
    public void setVertexUID(AInstructionCDFGNode vertex) {
        
        Map<String, Integer> currentUIDMap = this.getCurrentUIDMap();

        vertex.setUID(currentUIDMap.containsKey(vertex.getReference()) ? currentUIDMap.get(vertex.getReference()) + 1 : 0);
    }
    
    @Override
    public void suppressVertex(AInstructionCDFGNode vertex) {
        
        this.incomingEdgesOf(vertex).forEach((in_edge) -> {
            this.outgoingEdgesOf(vertex).forEach((out_edge) -> {
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