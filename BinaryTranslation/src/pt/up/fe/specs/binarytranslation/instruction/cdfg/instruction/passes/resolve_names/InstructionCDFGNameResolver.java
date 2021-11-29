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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.resolve_names;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.InstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGVariableNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.operation.arithmetic.InstructionCDFGAssignmentNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.AInstructionCDFGControlFlowConditionalSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.visitor.InstructionCDFGVisitor;

public class InstructionCDFGNameResolver extends InstructionCDFGVisitor<Map<String, AInstructionCDFGNode>>{

    private InstructionCDFGNameResolver(InstructionCDFG icdfg) {
        super(icdfg);
    }
    
    public static void resolve(InstructionCDFG icdfg) {
        InstructionCDFGNameResolver resolver = new InstructionCDFGNameResolver(icdfg);
        resolver.begin();
    }
    
    @Override
    protected Map<String, AInstructionCDFGNode> visitDataFlowSubgraph(InstructionCDFGDataFlowSubgraph subgraph) {
        this.resolveInputs(subgraph);
        return super.visitDataFlowSubgraph(subgraph);
    }
    
    @Override
    protected Map<String, AInstructionCDFGNode> visitAInstructionCDFGControlFlowConditionalSubgraph(AInstructionCDFGControlFlowConditionalSubgraph subgraph) {
        
        this.resolveInputs(subgraph);
        return super.visitAInstructionCDFGControlFlowConditionalSubgraph(subgraph);
    }
    
    @Override
    protected Map<String, AInstructionCDFGNode> visitControlMergeSubgraph(InstructionCDFGControlFlowMerge subgraph) {
        /*
         * Resolve previous nodes outputs
         */
        return super.visitControlMergeSubgraph(subgraph);
    }
 
    
     public Map<String, Integer> resolveInputs(Map<String, Integer> UIDMap, AInstructionCDFGSubgraph subgraph) {
         
         Map<String, Integer> newUIDMap = new HashMap<>();
         newUIDMap.putAll(UIDMap);
         
         subgraph.getInputs().forEach(input -> {
             
             newUIDMap.putIfAbsent(input.getReference(), 0);
             input.setUID(String.valueOf(newUIDMap.get(input.getReference())));
         
         });
         
         return newUIDMap;
     }
     
     public Map<String, Integer> resolveOutputs(Map<String, Integer> UIDMap, AInstructionCDFGSubgraph subgraph) {
         
         Map<String, Integer> newUIDMap = new HashMap<>();
         newUIDMap.putAll(UIDMap);
         
         subgraph.getOutputs().forEach(output -> {
             
             UIDMap.putIfAbsent(output.getReference(), 0);
             output.setUID(String.valueOf(UIDMap.get(output.getReference())));
         
         });
         
         return newUIDMap;
     }
     
     public void addNewAssignments(Map<String, String> newAssignments, InstructionCDFGDataFlowSubgraph subgraph) {
         
         newAssignments.forEach((input, output) -> {
             
             InstructionCDFGVariableNode inputNode = new InstructionCDFGVariableNode(input);
             InstructionCDFGVariableNode outputNode = new InstructionCDFGVariableNode(output);
             InstructionCDFGAssignmentNode assignmentNode = new InstructionCDFGAssignmentNode();
             
             subgraph.addVertices(Set.of(inputNode, outputNode, assignmentNode));
             subgraph.addEdge(inputNode, assignmentNode, new InstructionCDFGEdge());
             subgraph.addEdge(assignmentNode, outputNode, new InstructionCDFGEdge());
             
         });
         
     }
  
}
