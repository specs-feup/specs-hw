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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction;

import java.util.*;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.*;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.conditional.InstructionCDFGFalseEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.conditional.InstructionCDFGTrueEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.data.AInstructionCDFGDataNode;

public class InstructionCDFG extends ControlAndDataFlowGraph<GeneralFlowGraph, DataFlowGraph, ControlFlowNode, AInstructionCDFGEdge>{

    
    public InstructionCDFG() {
        super(DataFlowGraph.class, ControlFlowNode.class, AInstructionCDFGEdge.class);
      
    }
    
    public List<AInstructionCDFGNode> getUniqueOutputs(){
        
        
        Predicate<GeneralFlowGraph> is_data_flow = g -> g.getClass().isInstance(DataFlowGraph.class);
        //Predicate<AInstructionCDFGDataNode> is_unique = v -> !unique_list.contains(v);
        
        
        
        Set<AInstructionCDFGNode> node_set = new HashSet<>();
        
        this.vertexSet().forEach((graph) -> graph.vertexSet().forEach(vertex -> node_set.add((AInstructionCDFGNode) vertex)));
        
       
        
        return new ArrayList<AInstructionCDFGNode>(node_set);
    }
    
    public List<AInstructionCDFGNode> getUniqueInputs(){
    
        List<AInstructionCDFGNode> unique_inputs = new ArrayList<>();
        
        this.vertexSet().forEach((vertex) ->{
            
            if(vertex instanceof DataFlowGraph) {
                
                DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> dfg = (DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> )vertex;
                
                dfg.getInputs().forEach((input) -> {
                   if(!unique_inputs.contains(input)) {
                       unique_inputs.add(input);
                   }
                });
                
            }
            
        });
        return unique_inputs;
    }
    
    public void addVertexes(GeneralFlowGraph ... vertexes) {
       super.addVertexes(Arrays.asList(vertexes));
    }
    
    public void addControlEdgesTo(ControlFlowNode decision, GeneralFlowGraph path_true, GeneralFlowGraph path_false) {
        super.addEdge(decision, path_true, new InstructionCDFGTrueEdge());
        super.addEdge(decision, path_false, new InstructionCDFGFalseEdge());
    }
    
    public void addIncomingEdgesTo(GeneralFlowGraph target, GeneralFlowGraph ... sources) {
        super.addIncomingEdgesTo(target, Arrays.asList(sources));
    }

    public void addOutgoingEdgesTo(GeneralFlowGraph source, GeneralFlowGraph ... targets) {
        super.addOutgoingEdgesTo(source, Arrays.asList(targets));
    }
}
