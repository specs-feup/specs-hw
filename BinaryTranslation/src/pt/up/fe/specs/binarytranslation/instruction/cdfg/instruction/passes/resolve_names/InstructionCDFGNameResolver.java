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
import java.util.Map;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.AInstructionCDFGControlFlowConditionalSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIf;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIfElse;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.AInstructionCDFGControlFlowMergeSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.visitor.InstructionCDFGVisitor;

public class InstructionCDFGNameResolver extends InstructionCDFGVisitor<Map<String, AInstructionCDFGNode>>{
    
    private final Map<String, Integer> SSAMap;
    private final Map<AInstructionCDFGControlFlowMergeSubgraph, Set<String>> phiFunction;
     
    private InstructionCDFGNameResolver(InstructionCDFG icdfg) {
        super(icdfg);
        this.SSAMap = new HashMap<>();
        this.phiFunction = new HashMap<>();
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
        // TODO Auto-generated method stub
        return super.visitControlMergeSubgraph(subgraph);
    }
    
    
    @Override
    protected Map<String, AInstructionCDFGNode> visitControlIfSubgraph(InstructionCDFGControlFlowIf subgraph) {
        
        return super.visitControlIfSubgraph((InstructionCDFGControlFlowIf) this.convertIftoIfElse(subgraph));
    }
    
     public void resolveInputs(AInstructionCDFGSubgraph subgraph) {
         
         subgraph.getInputs().forEach(input -> {
             this.SSAMap.putIfAbsent(input.getReference(), 0);
             input.setUID(String.valueOf(this.SSAMap.get(input.getReference())));
         });
         
     }
     
     public void resolveOutputs(AInstructionCDFGSubgraph subgraph) {
         subgraph.getOutputs().forEach(output -> {
             this.SSAMap.putIfAbsent(output.getReference(), 0);
             output.setUID(String.valueOf(this.SSAMap.get(output.getReference())));
         });
     }
     
     public void addNewAssignment() {
         
     }
     
     public void renameOutput(Map<String, Integer> UIDs, InstructionCDFGDataFlowSubgraph subgraph) {
         /*
         UIDs.forEach((reference, uid) -> {
             subgraph.getOu
         });
         */
     }
     
     public AInstructionCDFGSubgraph convertIftoIfElse(InstructionCDFGControlFlowIf subgraph) {
         
         AInstructionCDFGSubgraph newSubgraph =  new InstructionCDFGControlFlowIfElse(subgraph.getMerge(), subgraph.getUIDMap());
         
         this.icdfg.addVertex(newSubgraph);
         
         this.icdfg.getVerticesBefore(subgraph).forEach(before -> this.icdfg.addEdge(before, newSubgraph, this.icdfg.getEdge(before, subgraph).duplicate()));

         
         this.icdfg.addControlEdgesTo(newSubgraph, this.icdfg.getTruePath(subgraph), this.icdfg.getFalsePath(subgraph));
         
         subgraph.copyVerticesTo(newSubgraph);
         
         //this.icdfg.removeVertex(subgraph);
         

         return subgraph;
     }
     
     
}
