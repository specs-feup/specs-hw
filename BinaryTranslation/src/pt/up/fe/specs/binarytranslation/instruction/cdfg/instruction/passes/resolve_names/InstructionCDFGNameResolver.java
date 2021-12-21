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

import java.util.HashMap;
import java.util.HashSet;
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
    
    protected Map<String, AInstructionCDFGNode> visitDataFlowSubgraph(InstructionCDFGDataFlowSubgraph subgraph, Map<String, Integer> uidMap) {

        this.resolveOutputs(subgraph, this.resolveInputs(subgraph, uidMap));

        return this.visitDataFlowSubgraph(subgraph);
    }
    
    protected Map<String, AInstructionCDFGNode> visitAInstructionCDFGControlFlowConditionalSubgraph(AInstructionCDFGControlFlowConditionalSubgraph subgraph, Map<String, Integer> uidMap) {
        
        this.resolveInputs(subgraph, uidMap);
        
        
        
        return super.visitAInstructionCDFGControlFlowConditionalSubgraph(subgraph);
    }
    
    protected Map<String, AInstructionCDFGNode> visitControlMergeSubgraph(InstructionCDFGControlFlowMerge subgraph, Map<String, Integer> pathTrueUIDMap, Map<String, Integer> pathFalseUIDMap) {
     
        Map<String, Integer> outputsToBeResolved = new HashMap<>(); // to resolve previous subgraphs outputs
        Map<String, Integer> mergedUIDMap = new HashMap<>(); // to pass to child
        
        Set<String> declaredOutputs = new HashSet<>();
        
        declaredOutputs.addAll(pathTrueUIDMap.keySet());
        declaredOutputs.addAll(pathFalseUIDMap.keySet());
        
        declaredOutputs.forEach(reference -> {
            
            if(pathTrueUIDMap.containsKey(reference) && pathFalseUIDMap.containsKey(reference)) {
               
                if(pathTrueUIDMap.get(reference).intValue() == pathFalseUIDMap.get(reference).intValue()) {
                    mergedUIDMap.put(reference, pathTrueUIDMap.get(reference).intValue());
                }else {
                    
                }
                
            }else if(!pathTrueUIDMap.containsKey(reference)) {
                mergedUIDMap.put(reference, pathFalseUIDMap.get(reference));
            }else if(!pathFalseUIDMap.containsKey(reference)) {
                mergedUIDMap.put(reference, pathTrueUIDMap.get(reference));
            }

        }); 
        
        return super.visitControlMergeSubgraph(subgraph);
    }

    
     public Map<String, Integer> resolveOutputs(AInstructionCDFGSubgraph subgraph, Map<String, Integer> UIDMap) {
         
         Map<String, Integer> newUIDMap = new HashMap<>();
         newUIDMap.putAll(UIDMap);
         
         subgraph.getOutputs().forEach(output -> {
             
             String outputReference = output.getReference();
             
             if(newUIDMap.putIfAbsent(outputReference, 0) != null) {
                 newUIDMap.replace(outputReference, newUIDMap.get(outputReference) + 1);
             }
             output.setUID(newUIDMap.get(outputReference));
         
         });
         
         return newUIDMap;
     }
     
     public Map<String, Integer> resolveInputs(AInstructionCDFGSubgraph subgraph, Map<String, Integer> UIDMap) {
         
         Map<String, Integer> newUIDMap = new HashMap<>();
         newUIDMap.putAll(UIDMap);
         
         subgraph.getInputs().forEach(input -> {
             
             String inputReference = input.getReference();
             
             UIDMap.putIfAbsent(inputReference, 0);
             input.setUID(UIDMap.get(inputReference));
         
         });
         
         return newUIDMap;
     }
     
     public void addNewAssignments(Map<String, Integer> newAssignments, InstructionCDFGDataFlowSubgraph subgraph) {
         
         newAssignments.forEach((reference, uid) -> {
             
             InstructionCDFGAssignmentNode assignmentNode = new InstructionCDFGAssignmentNode();
             
             InstructionCDFGVariableNode inputNode = null;
             
             if(!subgraph.getInputs().contains(reference)) {
                 inputNode = new InstructionCDFGVariableNode(reference);
                 inputNode.setUID(uid);
                 subgraph.addVertex(inputNode);
             }else {
                 
                 for(AInstructionCDFGNode input : subgraph.getInputs()){
                     if(input.getReference().equals(reference)) {
                         inputNode = (InstructionCDFGVariableNode) input;
                     }
                 }
             }
             
             InstructionCDFGVariableNode outputNode = null;
             
             if(!subgraph.getOutputs().contains(reference)) {
                 outputNode = new InstructionCDFGVariableNode(reference);
                 outputNode.setUID(uid++);
                 subgraph.addVertex(outputNode);
             }else {
                 for(AInstructionCDFGNode output: subgraph.getOutputs()){
                     if(output.getReference().equals(reference)) {
                         outputNode = (InstructionCDFGVariableNode) output;
                     }
                 } 
             }
             
             
             subgraph.addEdge(inputNode, assignmentNode, new InstructionCDFGEdge());
             subgraph.addEdge(assignmentNode, outputNode, new InstructionCDFGEdge());
             
         });
         
     }
  
}
