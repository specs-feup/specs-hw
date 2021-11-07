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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.controlanddataflowgraph.ControlAndDataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.InstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGFalseEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGTrueEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGGeneratedVariable;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGLiteralNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AInstructionCDFGControlFlowSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.AInstructionCDFGControlFlowConditionalSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class InstructionCDFG extends ControlAndDataFlowGraph<AInstructionCDFGSubgraph, AInstructionCDFGEdge>{

    private Instruction instruction;
    
    private Map<AInstructionCDFGNode, AInstructionCDFGSubgraph> dataInputs;
    private Map<AInstructionCDFGNode, AInstructionCDFGSubgraph> dataOutputs;
    
    private static Predicate<AInstructionCDFGNode> notGeneratedVariableNode = v -> !(v instanceof InstructionCDFGGeneratedVariable);
    private static Predicate<AInstructionCDFGNode> notLiteralNode = v -> !(v instanceof InstructionCDFGLiteralNode);
    private static Predicate<AInstructionCDFGNode> notControlNode = v -> !(v instanceof InstructionCDFGGeneratedVariable);
    
    public InstructionCDFG(Instruction instruction) {
        super(InstructionCDFGDataFlowSubgraph.class, AInstructionCDFGControlFlowSubgraph.class, AInstructionCDFGEdge.class);
        
        this.instruction = instruction;
        
        this.dataInputs = new HashMap<>();
        this.dataOutputs = new HashMap<>();
    }
    
    public Instruction getInstruction() {
        return this.instruction;
    }
    
    public boolean isControlFlowConditionNode(AInstructionCDFGSubgraph node) {
        
        for(AInstructionCDFGSubgraph next : this.getVerticesAfter(node)) {
            
            if(next instanceof AInstructionCDFGControlFlowSubgraph) {
                return true;
            }
            
        }
        
        return false;
    }
    
    public void generateDataInputs() {
        
        this.vertexSet().forEach(g -> g.getInputs().stream()
                .filter(InstructionCDFG.notLiteralNode.and(InstructionCDFG.notGeneratedVariableNode))
                .collect(Collectors.toSet()).forEach(i -> this.dataInputs.put(i, g))
                );

    }
    
    public Set<AInstructionCDFGNode> getDataInputs(){
        return this.dataInputs.keySet();
    }
    
    public Map<AInstructionCDFGNode, AInstructionCDFGSubgraph> getDataInputsMap(){
        return this.dataInputs;
    }
    
    public Set<String> getDataInputsNames(){
        Set<String> names = new HashSet<>();
        
        this.getDataInputs().forEach(name -> names.add(name.getUID()));
        
        return names;
    }
    
    public void generateDataOutputs() {
       
        this.vertexSet().forEach(g -> g.getOutputs().stream()
                .filter(InstructionCDFG.notGeneratedVariableNode.and(InstructionCDFG.notControlNode))
                .collect(Collectors.toSet()).forEach(o -> this.dataOutputs.put(o, g))
            );
    }
    
    public Set<AInstructionCDFGNode> getDataOutputs(){
        return this.dataOutputs.keySet();
    }
    
    public Map<AInstructionCDFGNode, AInstructionCDFGSubgraph> getDataOutputsMap(){
        return this.dataOutputs;
    }
    
    public Set<String> getDataOutputsNames(){
        Set<String> names = new HashSet<>();
        
        this.getDataOutputs().forEach(name -> names.add(name.getUID()));
        
        return names;
    }
  
    @Override
    public AInstructionCDFGEdge addEdge(AInstructionCDFGSubgraph sourceVertex, AInstructionCDFGSubgraph targetVertex) {
        AInstructionCDFGEdge edge = new InstructionCDFGEdge();
        this.addEdge(sourceVertex, targetVertex, edge);
        return edge;
    }
    
    @Override
    public void addControlEdgesTo(AInstructionCDFGSubgraph decision,AInstructionCDFGSubgraph path_true,AInstructionCDFGSubgraph path_false) {
        this.addEdge(decision, path_true, new InstructionCDFGTrueEdge());
        this.addEdge(decision, path_false, new InstructionCDFGFalseEdge());
    }

    
    public AInstructionCDFGSubgraph getTruePath(AInstructionCDFGControlFlowConditionalSubgraph vertex){
        
        for(AInstructionCDFGEdge e : this.outgoingEdgesOf(vertex)) {
            if(e instanceof InstructionCDFGTrueEdge) {
                return this.getEdgeTarget(e);
            }
        }
        
        return null;
    }
    
    public AInstructionCDFGSubgraph getFalsePath(AInstructionCDFGControlFlowConditionalSubgraph vertex){
        
        for(AInstructionCDFGEdge e : this.outgoingEdgesOf(vertex)) {
            if(e instanceof InstructionCDFGFalseEdge) {
                return this.getEdgeTarget(e);
            }
        }
        
        return null;
    }

    @Override
    public String toString() {
        return this.instruction.getName();
    }
    
}
