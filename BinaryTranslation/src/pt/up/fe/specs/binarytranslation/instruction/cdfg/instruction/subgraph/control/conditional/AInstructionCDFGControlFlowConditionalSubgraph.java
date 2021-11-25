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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional;

import java.util.Map;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AInstructionCDFGControlFlowSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;

public class AInstructionCDFGControlFlowConditionalSubgraph extends AInstructionCDFGControlFlowSubgraph{

    private InstructionCDFGControlFlowMerge merge;
    
    public static Predicate<AInstructionCDFGSubgraph> predicate = s -> s instanceof AInstructionCDFGControlFlowConditionalSubgraph;
    
    public AInstructionCDFGControlFlowConditionalSubgraph(InstructionCDFGControlFlowMerge merge, Map<String, Integer> uid_map) {
        super(uid_map);
        this.merge = merge;
    }
    
    public InstructionCDFGControlFlowMerge getMerge() {
        return this.merge;
    }
    
}
