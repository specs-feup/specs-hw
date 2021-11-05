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

import java.util.HashSet;
import java.util.Set;
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

    public InstructionCDFG(Instruction instruction) {
        super(InstructionCDFGDataFlowSubgraph.class, AInstructionCDFGControlFlowSubgraph.class, AInstructionCDFGEdge.class);
        
        this.instruction = instruction;
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
    
    public Set<AInstructionCDFGNode> getDataInputs(){
        
        Set<AInstructionCDFGNode> data_inputs = new HashSet<>();
        
        this.vertexSet().forEach(g -> data_inputs.addAll(g.getInputs().stream().
                filter(v -> !(v instanceof InstructionCDFGLiteralNode)).
                filter(v -> !(v instanceof InstructionCDFGGeneratedVariable)).
                collect(Collectors.toSet())
                ));

        return data_inputs;
    }
    
    public Set<AInstructionCDFGNode> getDataOutputs(){
        
        Set<AInstructionCDFGNode> data_outputs = new HashSet<>();
        
        this.vertexSet().forEach(g -> data_outputs.addAll(g.getOutputs()));
        return data_outputs;
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

}
