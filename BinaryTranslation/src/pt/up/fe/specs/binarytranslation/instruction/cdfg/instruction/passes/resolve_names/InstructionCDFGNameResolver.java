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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.control.AInstructionCDFGControlNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AInstructionCDFGControlFlowSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIf;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIfElse;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class InstructionCDFGNameResolver {

     public static void resolveNames(InstructionCDFG icdfg) {
            
            Map<String, Integer> previous_names = new HashMap<>();
            
            //start from outputs
            // go up and up
            // if a merge node is found garantee the outputs of the previous dfgs have the same name
            // when a condition node is found garantee the dfgs inputs have the same name
            icdfg.getOutputs().forEach(output -> InstructionCDFGNameResolver.visit(icdfg, previous_names, output));
    }
     
     public static void visit(InstructionCDFG icdfg, Map<String, Integer> previousNames, GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> node) {
        
         if(node instanceof InstructionCDFGControlFlowIf) {
             InstructionCDFGNameResolver.visitControlFlowIfNode(icdfg, previousNames, (InstructionCDFGControlFlowIf) node);
         }else if(node instanceof InstructionCDFGControlFlowIfElse) {
             InstructionCDFGNameResolver.visitControlFlowIfElseNode(icdfg, previousNames, (InstructionCDFGControlFlowIfElse) node);
         }else if(node instanceof InstructionCDFGControlFlowMerge) {
             InstructionCDFGNameResolver.visitControlFlowMergeNode(icdfg, previousNames, (InstructionCDFGControlFlowMerge) node);
         }else if(node instanceof InstructionCDFGDataFlowSubgraph) {
             InstructionCDFGNameResolver.visitDataFlowSubgraph(icdfg, previousNames, (InstructionCDFGDataFlowSubgraph) node);
         }

         icdfg.getVerticesBefore(node).forEach(subgraph -> InstructionCDFGNameResolver.visit(icdfg, previousNames, subgraph));
         
     }
     
     public static void visitDataFlowSubgraph(InstructionCDFG icdfg, Map<String, Integer> previousNames, InstructionCDFGDataFlowSubgraph dfg) {
         InstructionCDFGNameResolver.renameOutputRegisters(Set.of(dfg), previousNames); // uses the previous names map to rename the inputs
         InstructionCDFGNameResolver.renameInputRegisters(Set.of(dfg), previousNames); // 
     }
     
     
     @SuppressWarnings("unchecked")
    public static void visitControlFlowMergeNode(InstructionCDFG icdfg, Map<String, Integer> previousNames, InstructionCDFGControlFlowMerge merge) {
         
       //garantee outputs have the same name
         InstructionCDFGNameResolver.renameOutputRegisters(icdfg.getVerticesBefore(merge), previousNames);
         
     }
     
     public static void visitControlFlowIfNode(InstructionCDFG icdfg, Map<String, Integer> previousNames, InstructionCDFGControlFlowIf node) {
         
       //garantee inputs have the same name
         
         InstructionCDFGNameResolver.renameInputRegisters(Set.of(node), previousNames);
     }
    
     public static void visitControlFlowIfElseNode(InstructionCDFG icdfg, Map<String, Integer> previousNames, InstructionCDFGControlFlowIfElse node) {
         
         //garantee inputs have the same name
         
         InstructionCDFGNameResolver.renameInputRegisters(Set.of(node), previousNames);

         
     }
     
     @SuppressWarnings("unchecked")
    public static void renameOutputRegisters(Set<GeneralFlowGraph> subgraphs, Map<String, Integer> previousNames) {
         
         //Get List of all subgraphs inputs, but filter out control flow nodes

         subgraphs.forEach(candidate -> {
             
             if(!(candidate instanceof AInstructionCDFGControlFlowSubgraph)) {
             
                 candidate.getOutputs().forEach(output -> {

                         previousNames.putIfAbsent(((AInstructionCDFGNode) output).getReference(), 0);
                     
                         ((AInstructionCDFGNode) output).setUID(String.valueOf(previousNames.get(((AInstructionCDFGNode) output).getReference())));

                 });
             }
         });
         
     }
     
     public static void renameInputRegisters(Set<GeneralFlowGraph> subgraphs , Map<String, Integer> previousNames) {
         
         //Get List of all subgraphs inputs, but filter out control flow nodes
         
         List<AInstructionCDFGNode> subgraphInputs = new ArrayList<>();
         List<GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>> validCandidates = subgraphs.stream().filter(subgraph -> !(subgraph instanceof InstructionCDFGControlFlowMerge)).collect(Collectors.toList());
         
         Set<String> subgraphsHadNodeReference = new HashSet<>();
         
         validCandidates.forEach(candidate -> subgraphInputs.addAll(candidate.getInputs()));
         
         ((InstructionCDFGDataFlowSubgraph)subgraphs).getUIDMap().forEach((reference, uid) -> {
             
             if(subgraphsHadNodeReference.add(reference)) {
                 previousNames.replace(reference, previousNames.get(reference) + 1);
             }
             
             ((InstructionCDFGDataFlowSubgraph)subgraphs).getUIDMap().replace(reference, previousNames.get(reference));
         });;

         
     }
}
