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
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AInstructionCDFGControlFlowSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIf;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIfElse;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class InstructionCDFGNameResolver {
    
     public static void resolveNames(InstructionCDFG icdfg) {
            
            Map<String, Integer> previous_names = new HashMap<>();
            Map<InstructionCDFGControlFlowMerge, Map<AInstructionCDFGSubgraph, Boolean>> resolved = new HashMap<>();

  
            icdfg.getInputs().forEach(subgraph -> InstructionCDFGNameResolver.visit(icdfg, previous_names, resolved, subgraph));
    }
     

     public static void visit(InstructionCDFG icdfg, Map<String, Integer> previousNames,  Map<InstructionCDFGControlFlowMerge, Map<AInstructionCDFGSubgraph, Boolean>> resolved, AInstructionCDFGSubgraph node) {
        
         if(node instanceof InstructionCDFGControlFlowIf) {
             InstructionCDFGNameResolver.visitControlFlowIfNode(icdfg, previousNames, resolved, (InstructionCDFGControlFlowIf) node);
         }else if(node instanceof InstructionCDFGControlFlowIfElse) {
             InstructionCDFGNameResolver.visitControlFlowIfElseNode(icdfg, previousNames, resolved, (InstructionCDFGControlFlowIfElse) node);
         }else if(node instanceof InstructionCDFGControlFlowMerge) {
             InstructionCDFGNameResolver.visitControlFlowMergeNode(icdfg, previousNames, resolved, (InstructionCDFGControlFlowMerge) node);
         }else if(node instanceof InstructionCDFGDataFlowSubgraph) {
             InstructionCDFGNameResolver.visitDataFlowSubgraph(icdfg, previousNames, resolved, (InstructionCDFGDataFlowSubgraph) node);
         }

         icdfg.getVerticesAfter(node).forEach(subgraph -> InstructionCDFGNameResolver.visit(icdfg, previousNames, resolved, subgraph));
         
     }
     
     
     public static void visitDataFlowSubgraph(InstructionCDFG icdfg, Map<String, Integer> previousNames, Map<InstructionCDFGControlFlowMerge, Map<AInstructionCDFGSubgraph, Boolean>> resolved, InstructionCDFGDataFlowSubgraph dfg) {
         
         InstructionCDFGNameResolver.renameInputRegisters(Set.of(dfg), previousNames); 
         InstructionCDFGNameResolver.renameOutputRegisters(icdfg, Set.of(dfg), previousNames);  
         
     }
     
     
     @SuppressWarnings("unchecked")
    public static void visitControlFlowMergeNode(InstructionCDFG icdfg, Map<String, Integer> previousNames, Map<InstructionCDFGControlFlowMerge, Map<AInstructionCDFGSubgraph, Boolean>> resolved, InstructionCDFGControlFlowMerge merge) {

         InstructionCDFGNameResolver.renameOutputRegisters(icdfg, icdfg.getVerticesBefore(merge), previousNames);
     }
     
     public static void visitControlFlowIfNode(InstructionCDFG icdfg, Map<String, Integer> previousNames, Map<InstructionCDFGControlFlowMerge, Map<AInstructionCDFGSubgraph, Boolean>> resolved, InstructionCDFGControlFlowIf node) {
         
         InstructionCDFGNameResolver.renameInputRegisters(Set.of(node), previousNames);
         


     }
    
     public static void visitControlFlowIfElseNode(InstructionCDFG icdfg, Map<String, Integer> previousNames, Map<InstructionCDFGControlFlowMerge, Map<AInstructionCDFGSubgraph, Boolean>> resolved, InstructionCDFGControlFlowIfElse node) {
         
         InstructionCDFGNameResolver.renameInputRegisters(Set.of(node), previousNames);
         
         
     }
     
     @SuppressWarnings("unchecked")
    public static Set<String> renameOutputRegisters(InstructionCDFG icdfg, Set<AInstructionCDFGSubgraph> subgraphs, Map<String, Integer> previousNames) {
         
         //Get List of all subgraphs inputs, but filter out control flow nodes
         
         Set<String> modifiedRegisters = new HashSet<>();
         
         subgraphs.forEach(candidate -> {
             
             if(!(candidate instanceof AInstructionCDFGControlFlowSubgraph)) {
             
                 candidate.getOutputs().forEach(output -> {

                         previousNames.putIfAbsent(((AInstructionCDFGNode) output).getReference(), 0);
                       
                         ((AInstructionCDFGNode) output).setUID(String.valueOf(previousNames.get(((AInstructionCDFGNode) output).getReference()) + 1));
                         
                         modifiedRegisters.add(output.getReference());
                         
                 });
             }else if (candidate instanceof InstructionCDFGControlFlowMerge) {
                 modifiedRegisters.addAll(InstructionCDFGNameResolver.renameOutputRegisters(icdfg, icdfg.getVerticesBefore(candidate), previousNames));
             }
         });
         
         return modifiedRegisters;
     }
     
     
     public static void renameInputRegisters(Set<AInstructionCDFGSubgraph> subgraphs , Map<String, Integer> previousNames) {
         
         //Get List of all subgraphs inputs, but filter out control flow nodes
         
         subgraphs.stream().filter(subgraph -> !(subgraph instanceof InstructionCDFGControlFlowMerge)).forEach(subgraph -> {
             subgraph.getInputs().forEach(input -> {
                 previousNames.putIfAbsent(input.getReference(), 0);
                 input.setUID(String.valueOf(previousNames.get(input.getReference())));
             });
         });
         
     }
}
