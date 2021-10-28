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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data;

import java.util.Collection;
import java.util.HashMap;
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
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.port.InstructionCDFGInputPortNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.port.InstructionCDFGOutputPortNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.AInstructionCDFGOperationNode;

public class InstructionCDFGDataFlowSubgraph extends DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>{
 
    private Map<String, Integer> uid_map;
    
    public InstructionCDFGDataFlowSubgraph() {
        super(AInstructionCDFGDataNode.class, AInstructionCDFGOperationNode.class, AInstructionCDFGEdge.class);
        this.uid_map = new HashMap<>();
    }

    public InstructionCDFGDataFlowSubgraph(Map<String, Integer> uid_map) {
        super(AInstructionCDFGDataNode.class, AInstructionCDFGOperationNode.class, AInstructionCDFGEdge.class);
        this.uid_map = uid_map;
    }
    
    public void setUIDMap(Map<String, Integer> uid_map) {
        this.uid_map = uid_map;
    }
    
    /** Returns the UID Map of this graph's nodes
     * 
     * @return UID Map of this graph's node
     */
    public Map<String, Integer> getUIDMap(){
        return this.uid_map;
    }
    
    
    @Override
    public void generateOutputs() {
        this.setOutputs(this.getVertexStreamOfPredicate(v -> v instanceof AInstructionCDFGDataNode).filter(v -> !this.incomingEdgesOf(v).isEmpty()).collect(Collectors.toSet()));
    }
    
    public void setVertexUID(AInstructionCDFGNode vertex) {
        uid_map.put(vertex.getReference(), (uid_map.containsKey(vertex.getReference())) ? uid_map.get(vertex.getReference()) + 1 : 0);    
        vertex.setUID(String.valueOf(uid_map.get(vertex.getReference())));
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
        super.addVertices(vertices);
        vertices.forEach(v -> this.setVertexUID(v));
    }
    
    @Override
    public void addInput(AInstructionCDFGNode vertex) throws IllegalArgumentException{
        
        if(vertex instanceof InstructionCDFGInputPortNode) {
            super.addInput(vertex);
        }else {
            throw new IllegalArgumentException();
        }
        
    }

    @Override
    public void addOutput(AInstructionCDFGNode vertex) throws IllegalArgumentException{
        
        if(vertex instanceof InstructionCDFGOutputPortNode) {
            super.addOutput(vertex);
        }else {
            throw new IllegalArgumentException();
        }
        
    }
    /*
    @Override
    public void replaceVertex(AInstructionCDFGNode current, AInstructionCDFGNode replacement) throws IllegalArgumentException{
        
        if(!this.assertVertexExist(current) || !this.assertVertexExist(replacement)) {
            throw new IllegalArgumentException();
        }
        
        if(current instanceof InstructionCDFGInputPortNode) {
            if(!(replacement instanceof InstructionCDFGInputPortNode)) {
                throw new IllegalArgumentException();
            }else {
                this.getInputVertexList().replace(replacement);
            } 
        }else if(current instanceof InstructionCDFGOutputPortNode) {
            if(!(replacement instanceof InstructionCDFGOutputPortNode)) {
                throw new IllegalArgumentException();
            }else {
                this.getOutputVertexList().replace(replacement);
            } 
        }
        
        this.incomingEdgesOf(current).forEach(e -> {this.addEdge(this.getEdgeSource(e), replacement, e.duplicate());});
        this.outgoingEdgesOf(current).forEach(e -> {this.addEdge(replacement, this.getEdgeTarget(e), e.duplicate());});
        
        this.removeVertex(current);
    }
    */
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
        
        this.addVertex(operator);
        this.addVertex(operand);
        
        if(operand instanceof InstructionCDFGVariableNode) {
            this.addEdge(operand, operator, new InstructionCDFGUnaryOperandEdge(((InstructionCDFGVariableNode)operand).getModifiers())); 
        }else {
            this.addEdge(operand, operator, new InstructionCDFGUnaryOperandEdge());
        }
        
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

        this.addVertex(operator);
        this.addVertex(operandLeft);
        this.addVertex(operandRight);
        
        if(operandLeft instanceof InstructionCDFGVariableNode) {
            
           /* AInstructionCDFGNode operandLeftClean = new InstructionCDFGVariableNode(operandLeft.getReference());
            operandLeftClean.setUID(operandLeft.getUIDVal());
            super.addVertex(operandLeftClean);
            this.removeVertex(operandLeft);*/
            
            this.addEdge(operandLeft, operator, new InstructionCDFGLeftOperandEdge(((InstructionCDFGVariableNode)operandLeft).getModifiers())); 
        }else {
            this.addEdge(operandLeft, operator, new InstructionCDFGLeftOperandEdge());
        }
   
        if(operandRight instanceof InstructionCDFGVariableNode) {
            
            /*AInstructionCDFGNode operandRightClean = new InstructionCDFGVariableNode(operandRight.getReference());
            super.addVertex(operandRightClean);
            operandRightClean.setUID(operandRight.getUIDVal());
            this.removeVertex(operandRight);*/
            
            this.addEdge(operandRight, operator, new InstructionCDFGRightOperandEdge(((InstructionCDFGVariableNode)operandRight).getModifiers())); 
        }else {
            this.addEdge(operandRight, operator, new InstructionCDFGRightOperandEdge());
        }
        
        return operator;
    }
    
    
}
