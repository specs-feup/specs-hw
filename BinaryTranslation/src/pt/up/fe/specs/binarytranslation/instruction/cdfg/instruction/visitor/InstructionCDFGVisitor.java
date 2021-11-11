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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.visitor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.AInstructionCDFGControlFlowConditionalSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIf;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIfElse;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class InstructionCDFGVisitor<R> {

    private final InstructionCDFG icdfg;
    
    public InstructionCDFGVisitor(InstructionCDFG icdfg) {
        this.icdfg = icdfg;
    }
    
    public void begin() {
        this.visit(this.icdfg.getInputs());
    }
    
    protected R visit(AInstructionCDFGSubgraph subgraph) throws IllegalArgumentException{
        if(subgraph instanceof AInstructionCDFGControlFlowConditionalSubgraph) {
            return this.visitAInstructionCDFGControlFlowConditionalSubgraph((AInstructionCDFGControlFlowConditionalSubgraph) subgraph);
        }else if(subgraph instanceof InstructionCDFGControlFlowMerge) {
            return this.visitControlMergeSubgraph((InstructionCDFGControlFlowMerge) subgraph);
        }else if(subgraph instanceof InstructionCDFGDataFlowSubgraph) {
            return this.visitDataFlowSubgraph((InstructionCDFGDataFlowSubgraph) subgraph);
        }else {
            throw new IllegalArgumentException(subgraph.getClass().toString() + " is an invalid node to visit!");
        }
    }
    
    protected R visit(Set<AInstructionCDFGSubgraph> subgraphs) {
        
        subgraphs.forEach(subgraph -> {
        
            if(subgraph instanceof AInstructionCDFGControlFlowConditionalSubgraph) {
                 this.visitAInstructionCDFGControlFlowConditionalSubgraph((AInstructionCDFGControlFlowConditionalSubgraph) subgraph);
            }else if(subgraph instanceof InstructionCDFGControlFlowMerge) {
                 this.visitControlMergeSubgraph((InstructionCDFGControlFlowMerge) subgraph);
            }else if(subgraph instanceof InstructionCDFGDataFlowSubgraph) {
                 this.visitDataFlowSubgraph((InstructionCDFGDataFlowSubgraph) subgraph);
            }else {
                throw new IllegalArgumentException(subgraph.getClass().toString() + " is an invalid node to visit!");
            }
        
        });
        
        return null;
    }
    
    protected R visitDataFlowSubgraph(InstructionCDFGDataFlowSubgraph subgraph) {
        return this.visit(this.icdfg.getVerticesAfter(subgraph));   
    }
    
    protected R visitAInstructionCDFGControlFlowConditionalSubgraph(AInstructionCDFGControlFlowConditionalSubgraph subgraph) {

        if(subgraph instanceof InstructionCDFGControlFlowIf) {
            this.visitControlIfSubgraph((InstructionCDFGControlFlowIf) subgraph);
        }else if(subgraph instanceof InstructionCDFGControlFlowIfElse) {
            this.visitControlIfElseSubgraph((InstructionCDFGControlFlowIfElse) subgraph);
        }
        
        
        return this.visit(subgraph.getMerge());
    }
    
    protected R visitControlIfSubgraph(InstructionCDFGControlFlowIf subgraph) {  
        return this.visit(this.icdfg.getTruePath(subgraph));
    }
    
    protected R visitControlIfElseSubgraph(InstructionCDFGControlFlowIfElse subgraph) {
        return this.visit(Set.of(this.icdfg.getTruePath(subgraph), this.icdfg.getFalsePath(subgraph)));
    }
    
    protected R visitControlMergeSubgraph(InstructionCDFGControlFlowMerge subgraph) {
        return null;
    }
    
}
