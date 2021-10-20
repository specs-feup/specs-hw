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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction;

import java.util.Arrays;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.ControlAndDataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.ControlFlowNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.DataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.InstructionCDFGFalseEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.InstructionCDFGTrueEdge;

public class InstructionCDFG extends ControlAndDataFlowGraph<GeneralFlowGraph, DataFlowGraph, ControlFlowNode, AInstructionCDFGEdge>{

    
    public InstructionCDFG() {
        super(DataFlowGraph.class, ControlFlowNode.class, AInstructionCDFGEdge.class);
      
    }
    
    public void addVertexes(GeneralFlowGraph ... vertexes) {
       super.addVertexes(Arrays.asList(vertexes));
    }
    
    public void addControlEdgesTo(ControlFlowNode decision, GeneralFlowGraph path_true, GeneralFlowGraph path_false) {
        super.addEdge(decision, path_true, new InstructionCDFGTrueEdge());
        super.addEdge(decision, path_false, new InstructionCDFGFalseEdge());
    }
    
    public void addIncomingEdgesTo(GeneralFlowGraph target, GeneralFlowGraph ... sources) {
        super.addIncomingEdgesTo(target, Arrays.asList(sources));
    }

    public void addOutgoingEdgesTo(GeneralFlowGraph source, GeneralFlowGraph ... targets) {
        super.addOutgoingEdgesTo(source, Arrays.asList(targets));
    }
}
