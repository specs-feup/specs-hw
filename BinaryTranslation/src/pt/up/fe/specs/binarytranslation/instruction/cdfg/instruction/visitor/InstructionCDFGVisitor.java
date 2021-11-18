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

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.AInstructionCDFGControlFlowConditionalSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIf;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIfElse;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.AInstructionCDFGControlFlowMergeSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class InstructionCDFGVisitor<R> {

    protected final InstructionCDFG icdfg;
    
    public InstructionCDFGVisitor(InstructionCDFG icdfg) {
        this.icdfg = icdfg;
    }
    
    public void begin() {
        this.icdfg.getInputs().forEach(input -> this.visit(input));
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
    
    protected R visitDataFlowSubgraph(InstructionCDFGDataFlowSubgraph subgraph) {
        
        AInstructionCDFGSubgraph next;
        
        if(this.icdfg.hasVerticesAfter(subgraph))
            next = (AInstructionCDFGSubgraph) this.icdfg.getVerticesAfter(subgraph).toArray()[0];
        else
            return null;
        
        return (next instanceof AInstructionCDFGControlFlowMergeSubgraph) ? null : this.visit(next);   
    }
    
    protected R visitAInstructionCDFGControlFlowConditionalSubgraph(AInstructionCDFGControlFlowConditionalSubgraph subgraph) {

        if(subgraph instanceof InstructionCDFGControlFlowIf) {
            return this.visitControlIfSubgraph((InstructionCDFGControlFlowIf) subgraph);
        }else if(subgraph instanceof InstructionCDFGControlFlowIfElse) {
            return this.visitControlIfElseSubgraph((InstructionCDFGControlFlowIfElse) subgraph);
        }else {
            throw new IllegalArgumentException(subgraph.getClass().toString() + " is an invalid node to visit!");
        }
        
    }
    
    protected R visitControlIfSubgraph(InstructionCDFGControlFlowIf subgraph) {  
        this.visit(this.icdfg.getTruePath(subgraph));
        return this.visit(subgraph.getMerge());
    }
    
    protected R visitControlIfElseSubgraph(InstructionCDFGControlFlowIfElse subgraph) {
        
        this.visit(this.icdfg.getTruePath(subgraph));
        this.visit(this.icdfg.getFalsePath(subgraph));
        
        return this.visit(subgraph.getMerge());
    }
    
    protected R visitControlMergeSubgraph(InstructionCDFGControlFlowMerge subgraph) {
        return (!this.icdfg.hasVerticesAfter(subgraph)) ?  null : this.visit((AInstructionCDFGSubgraph) this.icdfg.getVerticesAfter(subgraph).toArray()[0]);
    }
    
}
