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

public class InstructionCDFGVisitor {

    private final InstructionCDFG icdfg;
  
    private Map<InstructionCDFGControlFlowMerge, Boolean> mergeResolved;
    
    public InstructionCDFGVisitor(InstructionCDFG icdfg) {
        this.icdfg = icdfg;
    }
    
    public void begin() {
        this.visit(this.icdfg.getInputs());
    }
    
    private void visit(Set<AInstructionCDFGSubgraph> subgraphs) {
        
        Set<AInstructionCDFGSubgraph> nextSubgraphs = new HashSet<>();
        
        for(AInstructionCDFGSubgraph sub : subgraphs) {
            if(sub instanceof InstructionCDFGControlFlowIf) {
                this.visitAInstructionCDFGControlFlowConditionalSubgraph((AInstructionCDFGControlFlowConditionalSubgraph) sub);
            }else if(sub instanceof InstructionCDFGControlFlowIfElse) {
                this.visitAInstructionCDFGControlFlowConditionalSubgraph((AInstructionCDFGControlFlowConditionalSubgraph) sub);
            }else if(sub instanceof InstructionCDFGControlFlowMerge) {
                if(this.mergeResolved.get((InstructionCDFGControlFlowMerge)sub) == true) {
                    this.visitControlMergeSubgraph((InstructionCDFGControlFlowMerge) sub);
                }else {
                    continue;
                }
            }else if(sub instanceof InstructionCDFGDataFlowSubgraph) {
                this.visitDataFlowSubgraph((InstructionCDFGDataFlowSubgraph) sub);
            }
            
            nextSubgraphs.addAll(this.icdfg.getVerticesAfter(sub));
        }
        
        this.visit(nextSubgraphs);
    }
    
    
    public void visitDataFlowSubgraph(InstructionCDFGDataFlowSubgraph subgraph) {
        
    }
    
    private void visitAInstructionCDFGControlFlowConditionalSubgraph(AInstructionCDFGControlFlowConditionalSubgraph subgraph) {
        
        this.mergeResolved.putIfAbsent(subgraph.getMerge(), null);
        
        if(subgraph instanceof InstructionCDFGControlFlowIf) {
            this.visitControlIfSubgraph((InstructionCDFGControlFlowIf) subgraph);
        }else if(subgraph instanceof InstructionCDFGControlFlowIfElse) {
            this.visitControlIfElseSubgraph((InstructionCDFGControlFlowIfElse) subgraph);
        }
        
    }
    
    public void visitControlIfSubgraph(InstructionCDFGControlFlowIf subgraph) {
        
        if(this.mergeResolved.get(subgraph.getMerge()) == null) {
            this.mergeResolved.replace(subgraph.getMerge(), false);
        }else if(this.mergeResolved.get(subgraph.getMerge()) == false) {
            this.mergeResolved.remove(subgraph.getMerge(), true);
        }
        
    }
    
    public void visitControlIfElseSubgraph(InstructionCDFGControlFlowIfElse subgraph) {
        
    }
    
    public void visitControlMergeSubgraph(InstructionCDFGControlFlowMerge subgraph) {
        
    }
    
}
