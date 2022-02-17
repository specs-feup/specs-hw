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

import java.util.ArrayList;
import java.util.Collection;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.AInstructionCDFGControlFlowConditionalSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIf;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIfElse;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class InstructionCDFGVisitor {
    
    protected InstructionCDFG icdfg;
    protected Collection<AInstructionCDFGSubgraph> subgraphsResolved;
    
    public InstructionCDFGVisitor(InstructionCDFG icdfg) {
        this.icdfg = icdfg;      
        this.subgraphsResolved = new ArrayList<>();
    }
    
    public void begin() {
        this.icdfg.getInputs().forEach(input -> this.visit(input));
    }
    
    /** Checks if the argument subgraph can be visited<br>
     *  A subgraph can be visited if all of the vertices before it have been visited before
     * 
     * @param subgraph Subgraph to check
     * @return True if the argument subgraph can be visited
     */
    protected boolean canVisitSubgraph(AInstructionCDFGSubgraph subgraph) {   
        
        if(this.icdfg.getVerticesBefore(subgraph).size() == 0) {
            return true;
        }
        

        return this.icdfg.getVerticesBefore(subgraph).stream().allMatch(subgraphBefore -> this.subgraphsResolved.contains(subgraphBefore));
    }
    
    /** Checks if the argument subgraph is a simple vertex<br>
     * A simple vertex has only a single vertex before and a single vertex after
     * 
     * @param subgraph Subgraph to check
     * @return True if the argument subgraph is a simple vertex
     */
    protected boolean isSimpleVertex(AInstructionCDFGSubgraph subgraph) {    
        return (!this.isSplitVertex(subgraph)) && (!this.isMergeVertex(subgraph));
    }
    
    /** Checks if the argument subgraph is a complex vertex<br>
     * A complex vertex has more than one vertex before and more than one vertex after, so it is both a split vertex and a merge vertex
     * 
     * @param subgraph Subgraph to check
     * @return True if the argument subgraph is a complex vertex
     */
    protected boolean isComplexVertex(AInstructionCDFGSubgraph subgraph) {
        return this.isSplitVertex(subgraph) && this.isMergeVertex(subgraph);
    }
    
    /** Checks if the argument subgraph is a split vertex<br>
     *  A split vertex has more than one vertex after
     * @param subgraph Subgraph to check
     * @return True if the argument subgraph is a split vertex
     */
    protected boolean isSplitVertex(AInstructionCDFGSubgraph subgraph) {
        return (this.icdfg.getVerticesAfter(subgraph).size() != 1);
    }
    
    /** Checks if the argument subgraph is a merge vertex<br>
     *  A merge vertex has more than one vertex before
     * 
     * @param subgraph Subgraph to check
     * @return True if the argument subgraph is a merge vertex
     */
    protected boolean isMergeVertex(AInstructionCDFGSubgraph subgraph) {
        return (this.icdfg.getVerticesBefore(subgraph).size() != 1);
    }
    
    protected void visit(AInstructionCDFGSubgraph subgraph) throws IllegalArgumentException{
        
  
            if(!this.canVisitSubgraph(subgraph)) {
                return;
            }else {
                this.subgraphsResolved.add(subgraph);
            }

            if(subgraph instanceof AInstructionCDFGControlFlowConditionalSubgraph) {
                 this.visitAInstructionCDFGControlFlowConditionalSubgraph((AInstructionCDFGControlFlowConditionalSubgraph) subgraph);
            }else if(subgraph instanceof InstructionCDFGControlFlowMerge) {
                 this.visitControlMergeSubgraph((InstructionCDFGControlFlowMerge) subgraph);
            }else if(subgraph instanceof InstructionCDFGDataFlowSubgraph) {
                 this.visitDataFlowSubgraph((InstructionCDFGDataFlowSubgraph) subgraph);
            }else {
                throw new IllegalArgumentException(subgraph.getClass().toString() + " is an invalid node to visit!");
            }

    }
    
    protected void visitDataFlowSubgraph(InstructionCDFGDataFlowSubgraph subgraph) {
        this.icdfg.getVerticesAfter(subgraph).forEach(subgraphAfter -> this.visit(subgraphAfter));
    }
    
    protected void visitAInstructionCDFGControlFlowConditionalSubgraph(AInstructionCDFGControlFlowConditionalSubgraph subgraph) throws IllegalArgumentException{

        if(subgraph instanceof InstructionCDFGControlFlowIf) {
             this.visitControlIfSubgraph((InstructionCDFGControlFlowIf) subgraph);
        }else if(subgraph instanceof InstructionCDFGControlFlowIfElse) {
             this.visitControlIfElseSubgraph((InstructionCDFGControlFlowIfElse) subgraph);
        }else {
            throw new IllegalArgumentException(subgraph.getClass().toString() + " is an invalid node to visit!");
        }
        
    }
    
    protected void visitControlIfSubgraph(InstructionCDFGControlFlowIf subgraph) {  
        
        this.visit(this.icdfg.getTruePath(subgraph));
        //this.visit(subgraph.getMerge());
    }
    
    protected void visitControlIfElseSubgraph(InstructionCDFGControlFlowIfElse subgraph) {
        
        this.visit(this.icdfg.getTruePath(subgraph));
        this.visit(this.icdfg.getFalsePath(subgraph));
       // this.visit(subgraph.getMerge());
    }
    
    protected void visitControlMergeSubgraph(InstructionCDFGControlFlowMerge subgraph) {
         this.icdfg.getVerticesAfter(subgraph).forEach(subgraphAfter -> this.visit(subgraphAfter));
    }
    
}
