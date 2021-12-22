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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.resolve_names.wip;

import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;

public class InstructionCDFGNameResolver {

    private InstructionCDFG icdfg;
    
    protected InstructionCDFGNameResolver(InstructionCDFG icdfg) {
        this.icdfg = icdfg;
    }
    
    public void resolve(InstructionCDFG icdfg) {
        InstructionCDFGNameResolver icdfg_resolver = new InstructionCDFGNameResolver(icdfg);
        
    }
    
    public void checkDataDependencyType(AInstructionCDFGSubgraph subgraph) {
        
        if(this.icdfg.getVerticesBefore(subgraph).size() != 1) {
            // DD merge
        }
        
        if(this.icdfg.getVerticesAfter(subgraph).size() != 1) {
            // DD split
        }
        
    }
    
    public void DataDependencyNormal() {
        
    }
    
    public void DataDependencySplit(AInstructionCDFGSubgraph subgraph, Map<String, Integer> UIDMap) {
        
        this.icdfg.getVerticesAfter(subgraph).forEach(subgraphAfter -> this.resolveInputs(subgraphAfter, UIDMap));
        
    }
    
    public void DataDependencyMerge(AInstructionCDFGSubgraph subgraph) {
        
        
        
    }
    
    private void resolveInputs(AInstructionCDFGSubgraph subgraph, Map<String, Integer> UIDMap) {
        
    }
    
    private void resolveOutputs(AInstructionCDFGSubgraph subgraph, Map<String, Integer> UIDMap) {
        
    }
    
    private void addMissingOutputs() {
        
    }
}
