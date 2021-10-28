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

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.controlanddataflowgraph.ControlAndDataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.InstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGFalseEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGTrueEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AInstructionCDFGControlFlowSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.AInstructionCDFGControlFlowConditionalSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class InstructionCDFG extends ControlAndDataFlowGraph<GeneralFlowGraph, AInstructionCDFGEdge>{

    private Set<AInstructionCDFGNode> data_inputs;
    private Set<AInstructionCDFGNode> data_outputs;
    private Instruction instruction;

    public InstructionCDFG(Instruction instruction) {
        super(InstructionCDFGDataFlowSubgraph.class, AInstructionCDFGControlFlowSubgraph.class, AInstructionCDFGEdge.class);
      
        this.data_inputs = new HashSet<>();
        this.data_outputs = new HashSet<>();
        
        this.instruction = instruction;
    }
    
    public Instruction getInstruction() {
        return this.instruction;
    }
    
    public boolean isControlFlowConditionNode(GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> node) {
        
        for(GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> next : this.getVerticesAfter(node)) {
            
            if(next instanceof AInstructionCDFGControlFlowSubgraph) {
                return true;
            }
            
        }
        
        return false;
    }
    
    public Set<AInstructionCDFGNode> getDataInputs(){
        
        this.vertexSet().forEach(g -> this.data_inputs.addAll(g.getInputs()));

        return this.data_inputs;
    }
    
    public Set<AInstructionCDFGNode> getDataOutputs(){
        this.vertexSet().forEach(g -> this.data_outputs.addAll(g.getOutputs()));
        return this.data_outputs;
    }
    
  
    @Override
    public AInstructionCDFGEdge addEdge(GeneralFlowGraph sourceVertex, GeneralFlowGraph targetVertex) {
        AInstructionCDFGEdge edge = new InstructionCDFGEdge();
        this.addEdge(sourceVertex, targetVertex, edge);
        return edge;
    }
    
    @Override
    public void addControlEdgesTo(GeneralFlowGraph decision,GeneralFlowGraph path_true,GeneralFlowGraph path_false) {
        this.addEdge(decision, path_true, new InstructionCDFGTrueEdge());
        this.addEdge(decision, path_false, new InstructionCDFGFalseEdge());
    }

    
    public GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> getTruePath(AInstructionCDFGControlFlowConditionalSubgraph vertex){
        
        for(AInstructionCDFGEdge e : this.outgoingEdgesOf(vertex)) {
            if(e instanceof InstructionCDFGTrueEdge) {
                return this.getEdgeTarget(e);
            }
        }
        
        return null;
    }
    
    public GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> getFalsePath(AInstructionCDFGControlFlowConditionalSubgraph vertex){
        
        for(AInstructionCDFGEdge e : this.outgoingEdgesOf(vertex)) {
            if(e instanceof InstructionCDFGFalseEdge) {
                return this.getEdgeTarget(e);
            }
        }
        
        return null;
    }

}
