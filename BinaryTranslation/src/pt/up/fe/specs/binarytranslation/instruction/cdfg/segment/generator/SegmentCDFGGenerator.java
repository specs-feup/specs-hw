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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.segment.generator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.InstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.generator.InstructionCDFGGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.segment.SegmentCDFG;

public class SegmentCDFGGenerator {

    public static SegmentCDFG generate(Collection<Instruction> instructions) {
        
        SegmentCDFG scdfg = new SegmentCDFG((List<Instruction>) instructions);
        
        instructions.forEach(instruction -> {
            
            InstructionCDFGGenerator icdfg_gen = new InstructionCDFGGenerator();
            InstructionCDFG icdfg = icdfg_gen.generate(instruction);
            
            Map<AInstructionCDFGNode, AInstructionCDFGSubgraph> previousOutputs = scdfg.getDataOutputsMap();
            
            Graphs.addGraph(scdfg, icdfg);
            
            previousOutputs.forEach((output, out_subgraph) -> {
                icdfg.getDataInputsMap().forEach((input, in_subgraph) -> {
              
                    if(output.getReference().equals(input.getReference())) {
                        scdfg.addEdge(out_subgraph, in_subgraph, new InstructionCDFGEdge());
                    }
                });
            });
            
            scdfg.refresh();
            
        });
        
        return scdfg;
    }
    
}
