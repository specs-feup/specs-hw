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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control;

import java.util.Map;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.control.AInstructionCDFGControlNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class AInstructionCDFGControlFlowSubgraph extends InstructionCDFGDataFlowSubgraph{

    private AInstructionCDFGControlNode control_vertex;
    
    public AInstructionCDFGControlFlowSubgraph(Map<String, Integer> uid_map) {
        super(uid_map);
        
    }
 
    public void setControlVertex(AInstructionCDFGControlNode control_vertex) {
        this.addVertex(control_vertex);
        this.control_vertex = control_vertex;
    }
    
    public AInstructionCDFGNode getControlVertex() {
        return this.control_vertex;
    }
    
    @Override
    public void isValidVertex(AInstructionCDFGNode vertex) throws IllegalArgumentException {
        return;
    }
    
    @Override
    public void generateOutputs() {
        this.setOutputs(this.getVertexStreamOfPredicate(v -> this.outgoingEdgesOf(v).isEmpty()).collect(Collectors.toSet()));
    }
}
