/**
 *  Copyright 2022 SPeCS.
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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.visitor.wip;

import java.util.HashSet;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIf;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIfElse;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class InstructionCDFGVisitor<A> {

    protected InstructionCDFG icdfg;
    private Set<AInstructionCDFGSubgraph> subgraphsResolved;
    
    public InstructionCDFGVisitor(InstructionCDFG icdfg) {
        
        this.icdfg = icdfg;
        this.subgraphsResolved = new HashSet<>();
        
    }
    
    public void begin(A initialVisitArgument) {
        this.icdfg.getInputs().forEach(input -> this.visit(input, initialVisitArgument));
    }
    
    public void begin() {
        this.icdfg.getInputs().forEach(input -> this.visit(input, null));
    }
    
    protected boolean canVisitSubgraph(AInstructionCDFGSubgraph subgraph) {   
        return (this.icdfg.getVerticesBefore(subgraph).size() == 0) ? true : !this.icdfg.getVerticesBefore(subgraph).stream().anyMatch(subgraphBefore -> !this.subgraphsResolved.contains(subgraphBefore));
    }
    
    protected void visit(AInstructionCDFGSubgraph subgraph, A visitArgument) throws IllegalArgumentException{
        
        if(!this.canVisitSubgraph(subgraph)) {
            return;
        }else {
            this.subgraphsResolved.add(subgraph);
        }

        if(subgraph instanceof InstructionCDFGControlFlowIf) {
             this.visitControlFlowIfSubgraph((InstructionCDFGControlFlowIf) subgraph, visitArgument);
        }else if(subgraph instanceof InstructionCDFGControlFlowMerge) {
            this.visitControlFlowIfElseSubgraph((InstructionCDFGControlFlowIfElse) subgraph, visitArgument);
        }else if(subgraph instanceof InstructionCDFGControlFlowMerge) {
             this.visitControlFlowMergeSubgraph((InstructionCDFGControlFlowMerge) subgraph, visitArgument);
        }else if(subgraph instanceof InstructionCDFGDataFlowSubgraph) {
             this.visitDataFlowSubgraph((InstructionCDFGDataFlowSubgraph) subgraph, visitArgument);
        }else {
            throw new IllegalArgumentException(subgraph.getClass().toString() + " is an invalid node to visit!");
        }
        
    }
    
    protected void visitDataFlowSubgraph(AInstructionCDFGSubgraph subgraph, A visitArgument) {
        this.icdfg.getVerticesAfter(subgraph).forEach(subgraphAfter -> this.visit(subgraph, visitArgument));
    }
    
    protected void visitControlFlowIfSubgraph(InstructionCDFGControlFlowIf subgraph, A visitArgument) {
        this.visit(this.icdfg.getTruePath(subgraph), visitArgument);
    }
    
    protected void visitControlFlowIfElseSubgraph(InstructionCDFGControlFlowIfElse subgraph, A visitArgument) {
        
        this.visit(this.icdfg.getTruePath(subgraph), visitArgument);
        this.visit(this.icdfg.getFalsePath(subgraph), visitArgument);
        
    }
    
    protected void visitControlFlowMergeSubgraph(InstructionCDFGControlFlowMerge subgraph, A visitArgument) {
        this.icdfg.getVerticesAfter(subgraph).forEach(subgraphAfter -> this.visit(subgraph, visitArgument));
    }
    
}
