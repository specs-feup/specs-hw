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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AInstructionCDFGControlFlowSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.AInstructionCDFGControlFlowConditionalSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIf;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIfElse;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.AInstructionCDFGControlFlowMergeSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.visitor.InstructionCDFGVisitor;

/**
 * Applies a pseudo SSA pass on the CDFG<br>
 * Due to the fact that the CDFG is used to generate HDL:<br>
 *      - Variables cannot assign themselves (this will be problematic if there is a jump instruction inside a DFG, but for now I think it's ok)<br>
 *      - In a conditonal, all the outputs before the merge need to have the same variable name, and if they are missing an assignment needs to be placed (needed for respecting the previous rule and to ensure proper coverage of conditionals (important for HDL design))<br>
 * 
 * @author João Conceição
 *
 */
public class InstructionCDFGNameResolver extends InstructionCDFGVisitor{
    
    private InstructionCDFGNameResolver(InstructionCDFG icdfg) {
        super(icdfg);
    }
    
    /** Resolves the names of the variables in an InstructionCDFG
     * 
     * @param icdfg InstructionCDFG to resolve
     */
    public static void resolve(InstructionCDFG icdfg) {
        InstructionCDFGNameResolver resolver = new InstructionCDFGNameResolver(icdfg);
        resolver.begin();
    }
    
    @Override
    protected void visitDataFlowSubgraph(InstructionCDFGDataFlowSubgraph subgraph) {
        
        this.setVertexInputs(subgraph, this.retrivePreviousUIDMap(subgraph)); // sets the subgraph inputs using the uid map calculated from the previous ones
        this.calculateVertexOutputs(subgraph, subgraph.getInputUIDMap());  // calculates the subgraph outputs from the input uid map
        System.out.println(subgraph.getCurrentUIDMap());
        super.visitDataFlowSubgraph(subgraph);
    }
    
    @Override
    protected void visitControlIfSubgraph(InstructionCDFGControlFlowIf subgraph) {
        
        
        this.setVertexInputs(subgraph, this.retrivePreviousUIDMap(subgraph));
        
        super.visitControlIfSubgraph(subgraph);
    }
    
    @Override
    protected void visitControlIfElseSubgraph(InstructionCDFGControlFlowIfElse subgraph) {

        this.setVertexInputs(subgraph, this.retrivePreviousUIDMap(subgraph));
        
        super.visitControlIfElseSubgraph(subgraph);
    }
    
    @Override
    protected void visitControlMergeSubgraph(InstructionCDFGControlFlowMerge subgraph) {
        
        this.retrivePreviousUIDMap(subgraph);
        
        super.visitControlMergeSubgraph(subgraph);
    }

    /** Sets the inputs of a subgraph
     * 
     * @param subgraph Subgraph to set the inputs of
     * @param currentUIDMap UIDMap of the inputs
     */
    protected void setVertexInputs(AInstructionCDFGSubgraph subgraph, Map<String, Integer> currentUIDMap) {
        subgraph.setInputUIDMap(currentUIDMap);
    }
    
    /** Sets the inputs of a collection of subgraphs
     * 
     * @param subgraphs Collection of subgraphs to set the inputs of
     * @param currentUIDMap UIDMap of the inputs
     */
    protected void setVerticesInputs(Collection<AInstructionCDFGSubgraph> subgraphs, Map<String, Integer> currentUIDMap) {
        subgraphs.forEach(subgraph -> this.setVertexInputs(subgraph, currentUIDMap));
    }
    
    /** Sets the outputs of a subgraph
     * 
     * @param subgraph Subgraph to set the outputs of
     * @param currentUIDMap UIDMap of the outputs
     */
    protected void setVertexOutputs(AInstructionCDFGSubgraph subgraph, Map<String, Integer> currentUIDMap) {
        subgraph.setOutputUIDMap(currentUIDMap);
    }
    
    /** Sets the outputs of a collection of subgraphs
     * 
     * @param subgraphs Collection of subgraphs to set the outputs of
     * @param currentUIDMap UIDMap of the outputs
     */
    protected void setVerticesOutputs(Collection<AInstructionCDFGSubgraph> subgraphs, Map<String, Integer> currentUIDMap) {
        subgraphs.forEach(subgraph -> this.setVertexOutputs(subgraph, currentUIDMap));
    }
    
    /** Calculates the outputs of a subgraph
     * 
     * @param subgraph Subgraph to calculate the outputs of
     * @param currentUIDMap UIDMap of the outputs
     */
    protected void calculateVertexOutputs(AInstructionCDFGSubgraph subgraph, Map<String, Integer> currentUIDMap) {
        subgraph.generateOutputUIDMap(currentUIDMap);
    }
    
    /** Calculates the outputs of a collection of subgraphs
     * 
     * @param subgraphs Collection of subgraphs to calculate the outputs of
     * @param currentUIDMap UIDMap of the outputs
     */
    protected void calculateVerticesOutputs(Collection<AInstructionCDFGSubgraph> subgraphs, Map<String, Integer> currentUIDMap) {
        subgraphs.forEach(subgraph -> this.calculateVertexOutputs(subgraph, currentUIDMap));
    }
   
    
    protected Map<String, Integer> retrivePreviousUIDMap(AInstructionCDFGSubgraph subgraph){
        
        Collection<Map<String, Integer>> previousSubgraphsUIDMaps = new ArrayList<>();
        
        this.icdfg.getVerticesBefore(subgraph).forEach(subgraphBefore -> {
            if(subgraphBefore instanceof AInstructionCDFGControlFlowConditionalSubgraph) {
                previousSubgraphsUIDMaps.add(subgraphBefore.getCurrentUIDMap());
            }else if(subgraphBefore instanceof AInstructionCDFGControlFlowMergeSubgraph){
                
                Collection<Map<String, Integer>> beforeMergeUIDMaps = new ArrayList<>();
                
                this.icdfg.getVerticesBefore(subgraph).forEach(subgraphBeforeMerge -> beforeMergeUIDMaps.add(this.retrivePreviousUIDMap(subgraphBeforeMerge)));

                previousSubgraphsUIDMaps.add(this.calculateCurrentUIDMap(beforeMergeUIDMaps));
            }
            else {
                previousSubgraphsUIDMaps.add(subgraphBefore.getCurrentUIDMap());
            }
            });
        
        Map<String, Integer> currentUIDMap = this.calculateCurrentUIDMap(previousSubgraphsUIDMaps);
        
        this.setVerticesOutputs(this.icdfg.getVerticesBefore(subgraph), currentUIDMap);
        
        return currentUIDMap;
    }
    
    /** Calculates the current UIDMap from a Collection of candidate UIDMaps<br>
     *  The current UIDMap has all of the references of the candidate UIDMaps, but the UID of each reference is the highest value from amongst all of the candidate UIDMaps
     * 
     * @param candidateUIDMaps Collection of candidate UIDMaps
     * @return The current UIDMap
     */
    protected Map<String, Integer> calculateCurrentUIDMap(Collection<Map<String, Integer>> candidateUIDMaps) {
        
        Map<String, Integer> currentUIDMap = new HashMap<String, Integer>();
        
        candidateUIDMaps.forEach(candidate -> {
            candidate.forEach((reference, uid) -> {
                
                currentUIDMap.putIfAbsent(reference, uid);
                
                if(currentUIDMap.get(reference).intValue() < uid.intValue()) {
                    currentUIDMap.replace(reference, uid);
                }
                
            });
        });
        
        return currentUIDMap;
    }
     
}
